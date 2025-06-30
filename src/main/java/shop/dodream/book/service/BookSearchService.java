package shop.dodream.book.service;


import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookSortType;

import java.util.List;



public interface BookSearchService {
    List<BookItemResponse> searchBooks(String keyword, BookSortType bookSortType);

}
