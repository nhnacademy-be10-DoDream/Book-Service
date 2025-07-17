package shop.dodream.book.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookSortType;

import java.util.List;


public interface BookSearchService {
    Page<BookItemResponse> searchBooks(String keyword, BookSortType bookSortType, Pageable pageable,
                                       List<Long> categoryIds, Integer minPrice, Integer maxPrice);

}
