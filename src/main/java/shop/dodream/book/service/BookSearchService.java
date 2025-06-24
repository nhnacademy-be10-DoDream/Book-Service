package shop.dodream.book.service;

import org.springframework.data.elasticsearch.core.SearchHits;
import shop.dodream.book.dto.BookDocument;
import shop.dodream.book.dto.BookSearchResponse;

public interface BookSearchService {
//    void indexAllBooks();
    BookSearchResponse searchBooks(String keyword);

}
