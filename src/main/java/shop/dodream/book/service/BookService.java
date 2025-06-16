package shop.dodream.book.service;

import shop.dodream.book.dto.*;


import java.util.List;

public interface BookService {

    BookRegisterResponse registerBookByIsbn(BookRegisterRequest request);

    List<BookListResponse> getAllBooks();

    AdminBookDetailResponse getBookByIdForAdmin(Long bookId);

    UserBookDetailResponse getBookByIdForUser(Long bookId);

    void updateBook(Long bookId, BookUpdateRequest request);

    void deleteBook(Long bookId);

    BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request);
}
