package shop.dodream.book.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.event.BookImageDeleteEvent;
import shop.dodream.book.core.properties.AladdinBookProperties;
import shop.dodream.book.core.properties.MinIOProperties;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookAdminListResponseRecord;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.Image;
import shop.dodream.book.exception.*;
import shop.dodream.book.infra.client.AladdinBookClient;
import shop.dodream.book.infra.dto.AladdinBookResponse;
import shop.dodream.book.repository.BookElasticsearchRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.BookDocumentUpdater;
import shop.dodream.book.service.BookService;
import shop.dodream.book.service.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AladdinBookClient aladdinBookClient;
    private final BookRepository bookRepository;
    private final AladdinBookProperties properties;
    private final BookElasticsearchRepository bookElasticsearchRepository;
    private final FileService fileService;
    private final BookDocumentUpdater bookDocumentUpdater;
    private final ApplicationEventPublisher eventPublisher;
    private final MinIOProperties minIOProperties;
    private final RedisTemplate<String, Long> redisTemplate;

    @Override
    @Transactional
    public void registerBookByIsbn(String isbn) {
        if (bookRepository.existsByIsbn(isbn)){
            throw new DuplicateIsbnException(isbn);
        }

        AladdinBookResponse aladdinBookResponse = aladdinBookClient.searchBook(
                properties.getTtbkey(),
                properties.getItemIdType(),
                isbn,
                properties.getOutput(),
                properties.getVersion(),
                properties.getCover()
        );

        if (aladdinBookResponse.getErrorCode() != null){
            throw new AladdinBookNotFoundException(isbn);
        }

        AladdinBookResponse.Item item = aladdinBookResponse.getItem().getFirst();

        String imageUrl = fileService.uploadBookImageFromUrl(item.getCover());

        try {
            Book book = item.toEntity();

            Image bookImage = new Image(book, imageUrl, true);
            book.addImages(List.of(bookImage));
            Book savedBook = bookRepository.save(book);

            bookElasticsearchRepository.save(new BookDocument(savedBook, imageUrl));
        }catch (Exception e) {
            eventPublisher.publishEvent(new BookImageDeleteEvent(List.of(imageUrl)));
            throw e;
        }


    }

    @Override
    @Transactional
    public void registerBookDirect(BookRegisterRequest registerRequest, List<MultipartFile> files) {
        if (bookRepository.existsByIsbn(registerRequest.getIsbn())){
            throw new DuplicateIsbnException(registerRequest.getIsbn());
        }

        Book book = registerRequest.toEntity();


        if (files == null || files.isEmpty()) {
            String defaultKey = minIOProperties.getDefaultImage();
            Image defaultImage = new Image(book, defaultKey, true);
            book.addImages(List.of(defaultImage));
        }

        List<String> uploadedImageKeys = fileService.uploadBookImageFromFiles(files);

        try {
            book.addImages(createBookImagesThumbnail(book,uploadedImageKeys));
            Book savedBook = bookRepository.save(book);
            String img = minIOProperties.getDefaultImage();
            if (!uploadedImageKeys.isEmpty()) {
                img = uploadedImageKeys.getFirst();
            }
            bookElasticsearchRepository.save(new BookDocument(savedBook, img));
        }catch (Exception e) {
            eventPublisher.publishEvent(new BookImageDeleteEvent(uploadedImageKeys));
            throw e;
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookAdminListResponseRecord> getAllBooks(Pageable pageable) {
        return bookRepository.findAllBy(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookAdminListResponseRecord> getAllBooks() {
        return bookRepository.findAllBy();
    }



    @Override
    @Transactional(readOnly = true)
    public List<BookListResponseRecord> findAllByIds(List<Long> ids) {
        return bookRepository.findVisibleBooksByIds(ids);
    }



    @Override
    @Transactional(readOnly = true)
    public BookDetailResponse getBookByIdForAdmin(Long bookId) {
        return bookRepository.findBookDetailForAdmin(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }


    @Override
    @Transactional(readOnly = true)
    public BookDetailResponse getBookByIdForUser(Long bookId) {
        return bookRepository.findBookDetailForUser(bookId)
                .orElseThrow(()-> new BookNotFoundException(bookId));
    }


    @Override
    @Transactional
    public void updateBook(Long bookId, BookUpdateRequest request, List<MultipartFile> files) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        if (book.getStatus() == BookStatus.REMOVED){
            throw new BookAlreadyRemovedException();
        }

        if (files != null && !files.isEmpty() && files.stream().anyMatch(file -> !file.isEmpty())) {
            List<String> deleteKeys = book.getImages().stream()
                    .map(Image::getUuid)
                    .filter(key -> !key.equals(minIOProperties.getDefaultImage()))
                    .toList();
            book.getImages().clear();

            List<String> uploadedKeys = fileService.uploadBookImageFromFiles(files);
            List<Image> newImages = createBookImagesThumbnail(book, uploadedKeys);
            book.addImages(newImages);

            eventPublisher.publishEvent(new BookImageDeleteEvent(deleteKeys));
        }

        book.updateTextFields(request);
        updateStatusByBookCount(book);

        Map<String, Object> updateMap = request.toUpdateMap();
        if (!updateMap.isEmpty()){
                bookDocumentUpdater.updateBookFields(bookId, updateMap);
        }


    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BookNotFoundException(bookId));
        book.setStatus(BookStatus.REMOVED);

        List<String> deleteKeys = book.getImages().stream()
                .map(Image::getUuid)
                .toList();

        eventPublisher.publishEvent(new BookImageDeleteEvent(deleteKeys));
//        bookElasticsearchRepository.deleteById(bookId);

    }

    @Override
    @Transactional
    public BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request) {
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(()-> new BookNotFoundException(request.getBookId()));
        if (book.getStatus() != BookStatus.SELL){
            throw new BookNotOrderableException();
        }

        Long currentStock = book.getBookCount();
        if (currentStock < request.getBookCount()){
            throw new BookCountNotEnoughException(currentStock);
        }

        book.setBookCount(currentStock-request.getBookCount());

        updateStatusByBookCount(book);

        return new BookCountDecreaseResponse(book.getId(), book.getBookCount(), book.getStatus() == BookStatus.SELL);
    }

    @Override
    @Transactional
    public void increaseBookCount(BookCountIncreaseRequest request) {
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new BookNotFoundException(request.getBookId()));


        Long currentStock = book.getBookCount();
        book.setBookCount(currentStock+request.getBookCount());

        updateStatusByBookCount(book);

    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }



    private void updateStatusByBookCount(Book book) {
        if (book.getStatus() != BookStatus.REMOVED) {
            long count = book.getBookCount();
            if (count == 0) {
                book.setStatus(BookStatus.SOLD_OUT);
            } else if (count <= 5) {
                book.setStatus(BookStatus.LOW_STOCK);
            } else {
                book.setStatus(BookStatus.SELL);
            }
        }
    }


    private List<Image> createBookImagesThumbnail(Book book, List<String> imageUrls) {
        List<Image> bookImages = new ArrayList<>(imageUrls.size());

        for(int i=0 ; i< imageUrls.size();i++){
            String imageUrl = imageUrls.get(i);
            boolean isThumbnail = (i==0);
            Image bookImage = new Image(book, imageUrl, isThumbnail);
            bookImages.add(bookImage);
        }

        return bookImages;
    }


    @Override
    public void increaseViewCount(Long bookId) {
        String key = "viewCount:book:"+bookId;
        redisTemplate.opsForValue().increment(key);
    }

    @Override
    public void registerBookListIsbn(IsbnListRequest request) {
        List<String> isbnList = request.getIsbnList();

        for(String isbn: isbnList){
            try{
                registerBookByIsbn(isbn);
            }catch (Exception e){
                log.error("Failed to register ISBN: {}", isbn, e);
            }


        }

    }
}
