package shop.dodream.book.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.projection.BookListResponseRecord;


public interface BookLikeQuerydslRepository {
    Page<BookListResponseRecord> findLikedBooksByUserId(String userId, Pageable pageable);
}
