package shop.dodream.book.service;

import shop.dodream.book.dto.*;

import java.util.List;

public interface BookService {

    BookRegisterResponse registerBookByIsbn(BookRegisterRequest request);

    List<BookListResponse> getAllBooks();

    BookDetailResponse getBookById(Long id);

    void updateBook(Long bookId, BookUpdateRequest request);

    void deleteBook(Long bookId);

    BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request);
}
