package shop.dodream.book.service;

import shop.dodream.book.dto.BookDetailResponse;
import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.dto.BookRegisterRequest;
import shop.dodream.book.dto.BookRegisterResponse;

import java.util.List;

public interface BookService {

    BookRegisterResponse registerBookByIsbn(BookRegisterRequest request);

    List<BookListResponse> getAllBooks();

    BookDetailResponse getBookById(Long id);
}
