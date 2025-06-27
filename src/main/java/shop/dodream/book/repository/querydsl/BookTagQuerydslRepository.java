package shop.dodream.book.repository.querydsl;

import org.springframework.data.repository.query.Param;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.Tag;

import java.util.List;

public interface BookTagQuerydslRepository {
    List<Tag> findAllByBookId(@Param("bookId") Long bookId);
    List<BookListResponseRecord> findBookListByTagId(Long tagId);
}
