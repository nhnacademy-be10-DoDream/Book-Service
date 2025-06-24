package shop.dodream.book.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.dodream.book.dto.BookDocument;

@Profile("!ci")
public interface BookElasticsearchRepository extends ElasticsearchRepository<BookDocument, Long> {
}
