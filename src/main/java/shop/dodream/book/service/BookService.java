package shop.dodream.book.service;

import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookListProjection;
import shop.dodream.book.dto.projection.UserBookDetailProjection;

import java.util.List;

public interface BookService {

    BookRegisterResponse registerBookByIsbn(BookRegisterRequest request);

    List<BookListProjection> getAllBooks();

    AdminBookDetailResponse getBookByIdForAdmin(Long bookId);

    UserBookDetailProjection getBookByIdForUser(Long bookId);

    void updateBook(Long bookId, BookUpdateRequest request);

    void deleteBook(Long bookId);

    BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request);
}
