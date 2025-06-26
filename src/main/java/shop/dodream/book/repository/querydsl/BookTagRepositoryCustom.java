package shop.dodream.book.repository.querydsl;

import org.springframework.data.repository.query.Param;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.Tag;

import java.util.List;

public interface BookTagRepositoryCustom {
    List<Tag> findAllByBookId(@Param("bookId") Long bookId);
    List<Book> findAllByTagId(@Param("tagId") Long tagId);
}
