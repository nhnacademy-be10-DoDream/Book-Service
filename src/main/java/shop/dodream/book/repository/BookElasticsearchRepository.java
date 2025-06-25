package shop.dodream.book.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.dodream.book.dto.BookDocument;
import shop.dodream.book.dto.BookItemResponse;

import java.util.List;

public interface BookElasticsearchRepository extends ElasticsearchRepository<BookDocument, Long> {


}
