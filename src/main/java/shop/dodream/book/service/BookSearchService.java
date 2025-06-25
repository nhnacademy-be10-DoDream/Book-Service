package shop.dodream.book.service;


import shop.dodream.book.dto.BookItemResponse;

import java.util.List;



public interface BookSearchService {
    List<BookItemResponse> searchBooks(String keyword, String sort);

}
