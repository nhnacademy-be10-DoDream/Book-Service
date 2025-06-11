package shop.dodream.book.service;

import shop.dodream.book.dto.BookRegisterRequest;
import shop.dodream.book.dto.BookRegisterResponse;

public interface BookService {

    BookRegisterResponse registerBookByIsbn(BookRegisterRequest request);
}
