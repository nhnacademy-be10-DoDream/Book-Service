package shop.dodream.book.service;

import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;

public interface BookService {

    void registerBookByIsbn(BookRegisterRequest request);

    List<BookListResponseRecord> getAllBooks();

    List<BookListResponseRecord> findAllByIds(List<Long>
                                                      ids);
    BookDetailResponse getBookByIdForAdmin(Long bookId);

    BookDetailResponse getBookByIdForUser(Long bookId);

    void updateBook(Long bookId, BookUpdateRequest request);

    void deleteBook(Long bookId);

    BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request);

}
