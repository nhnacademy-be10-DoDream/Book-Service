package shop.dodream.book.service;

import org.springframework.data.elasticsearch.core.SearchHits;
import shop.dodream.book.dto.BookDocument;
import shop.dodream.book.dto.BookSearchResponse;

import java.util.Map;

public interface BookSearchService {
    Map<String, Object> searchBooks(String keyword);

}
