package shop.dodream.book.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.dodream.book.dto.BookDocument;

public interface BookElasticsearchRepository extends ElasticsearchRepository<BookDocument, Long> {
}
