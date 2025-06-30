package shop.dodream.book.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dodream.book.core.event.ImageDeleteEvent;
import shop.dodream.book.core.properties.AladdinBookProperties;
import shop.dodream.book.dto.*;
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

import java.util.List;
import java.util.Map;


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

    @Override
    @Transactional
    public void registerBookByIsbn(BookRegisterRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())){
            throw new DuplicateIsbnException(request.getIsbn());
        }

        AladdinBookResponse aladdinBookResponse = aladdinBookClient.searchBook(
                properties.getTtbkey(),
                properties.getIsbn(),
                request.getIsbn(),
                properties.getOutput(),
                properties.getVersion()
        );

        if (aladdinBookResponse.getErrorCode() != null){
            throw new AladdinBookNotFoundException(request.getIsbn());
        }

        AladdinBookResponse.Item item = aladdinBookResponse.getItems().getFirst();

        String imageUrl = fileService.uploadBookImageFromUrl(item.getImage());
        try {
            Book book = request.toEntity(aladdinBookResponse);

            Image bookImage = new Image(book, imageUrl, true);
            book.addImages(List.of(bookImage));


            Book savedBook = bookRepository.save(book);

            bookElasticsearchRepository.save(new BookDocument(savedBook));
        }catch (Exception e) {
            eventPublisher.publishEvent(new ImageDeleteEvent(imageUrl));
            throw e;
        }

    }




    @Override
    @Transactional(readOnly = true)
    public List<BookListResponseRecord> getAllBooks() {
        return bookRepository.findAllBy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookListResponseRecord> findAllByIds(List<Long> ids) {
        return bookRepository.findVisibleBooksByIds(ids);
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
    public void updateBook(Long bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        if (book.getStatus() == BookStatus.REMOVED){
            throw new BookAlreadyRemovedException();
        }


        book.update(request);

        // 정가 or 할인가가 변경 되엇으면 할인율 재계산
        if (request.getRegularPrice() != null || request.getSalePrice() != null){
            Long regularPrice = request.getRegularPrice() != null ? request.getRegularPrice() : book.getRegularPrice();
            Long salePrice = request.getSalePrice() != null ? request.getSalePrice() : book.getSalePrice();

            if (salePrice > regularPrice) {
                throw new InvalidDiscountPriceException(salePrice, regularPrice);
            }
            if (regularPrice != null && regularPrice != 0){
                long discountRate = Math.round((1 - (double) salePrice / regularPrice) * 100);
                book.setDiscountRate(discountRate);
            }
        }


        updateStatusByBookCount(book);

        Map<String, Object> updateMap = request.toUpdateMap();
        if (!updateMap.isEmpty()){
            try {
                bookDocumentUpdater.updateBookFields(bookId, updateMap);
            }catch (Exception e){
                throw new RuntimeException("Elasticsearch 문서 업데이트 실패", e);
            }
        }


    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BookNotFoundException(bookId));
        book.setStatus(BookStatus.REMOVED);
        bookElasticsearchRepository.deleteById(bookId);

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

}
