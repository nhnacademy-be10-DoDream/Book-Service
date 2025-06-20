package shop.dodream.book.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dodream.book.config.BookMapper;
import shop.dodream.book.config.NaverBookProperties;
import shop.dodream.book.dto.*;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.exception.*;
import shop.dodream.book.infra.client.NaverBookClient;
import shop.dodream.book.infra.dto.NaverBookResponse;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.BookService;
import shop.dodream.book.util.MinioUploader;


import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final NaverBookClient naverBookClient;
    private final BookRepository bookRepository;
    private final NaverBookProperties properties;
    private final BookMapper bookMapper;
    private final MinioUploader minioUploader;

    @Override
    @Transactional
    public BookRegisterResponse registerBookByIsbn(BookRegisterRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())){
            throw new DuplicateIsbnException(request.getIsbn());
        }

        NaverBookResponse naverBookResponse = naverBookClient.searchBook(
                properties.getClientId(),
                properties.getClientSecret(),
                request.getIsbn()
        );

        List<NaverBookResponse.Item> items = naverBookResponse.getItems();
        if (items == null || items.isEmpty()){
            throw new NaverBookNotFoundException(request.getIsbn());
        }

        NaverBookResponse.Item item = items.getFirst();

        String uploadedImageUrl;
        try {
            uploadedImageUrl = minioUploader.uploadFromUrl(item.getImage());
        } catch (IOException e) {
            throw new MinioImageUploadException();
        }


        Book book = item.toPartialEntity();
        book.setBookUrl(uploadedImageUrl);
        request.applyTo(book);

        Book savedBook = bookRepository.save(book);


        return new BookRegisterResponse(savedBook);


    }


    @Override
    @Transactional(readOnly = true)
    public List<BookListResponse> getAllBooks() {
        return bookRepository.findAllBy();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminBookDetailResponse getBookByIdForAdmin(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotFoundException(bookId));
        return new AdminBookDetailResponse(book);
    }


    @Override
    @Transactional(readOnly = true)
    public UserBookDetailResponse getBookByIdForUser(Long bookId) {
        return bookRepository.findBookDetailForUserById(bookId).orElseThrow(()-> new BookIdNotFoundException(bookId));
    }


    @Override
    @Transactional
    public void updateBook(Long bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookIdNotFoundException(bookId));

        if (book.getStatus() == BookStatus.REMOVED){
            throw new BookAlreadyRemovedException();
        }

        // MapStruct로 기본 필드 매핑 널값은 기존값 유지
        bookMapper.updateBookFromDto(request, book);


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
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BookIdNotFoundException(bookId));
        book.setStatus(BookStatus.REMOVED);

    }

    @Override
    @Transactional
    public BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request) {
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(()-> new BookIdNotFoundException(request.getBookId()));
        if (book.getStatus() != BookStatus.SELL){
            throw new BookNotOrderableException();
        }

        Long currentStock = book.getBookCount();
        if (currentStock < request.getBookCount()){
            throw new BookCountNotEnoughException(currentStock);
        }

        book.setBookCount(currentStock-request.getBookCount());

        updateStatusByBookCount(book);

        BookCountDecreaseResponse decreaseResponse = new BookCountDecreaseResponse(book.getId(), book.getBookCount(), book.getStatus() == BookStatus.SELL);
        return decreaseResponse;

    }

    @Override
    @Transactional(readOnly = true)
    public BookLikeCountResponse getBookLikeCount(Long bookId) {
        return bookRepository.findLikeCountByBookId(bookId).orElseThrow(() -> new BookIdNotFoundException(bookId));

    }


    @Override
    @Transactional(readOnly = true)
    public List<BookListResponse> findAllByIds(List<Long> ids) {
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




}
