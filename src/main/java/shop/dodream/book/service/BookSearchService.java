package shop.dodream.book.service;


import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.BookItemWithCountResponse;
import shop.dodream.book.dto.BookSortType;

import java.util.List;


public interface BookSearchService {
    BookItemWithCountResponse searchBooks(String keyword,
                                          BookSortType bookSortType,
                                          Pageable pageable,
                                          List<Long> categoryIds,
                                          Integer minPrice,
                                          Integer maxPrice);

}
