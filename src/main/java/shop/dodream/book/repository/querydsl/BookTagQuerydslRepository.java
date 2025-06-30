package shop.dodream.book.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.Tag;

import java.util.List;

public interface BookTagQuerydslRepository {
    List<Tag> findAllByBookId(@Param("bookId") Long bookId);
    Page<BookListResponseRecord> findBookListByTagId(Long tagId, Pageable pageable);
}
