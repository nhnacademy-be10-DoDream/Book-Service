package shop.dodream.book.repository.querydsl;

import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;

public interface BookTagQuerydslRepository {
    List<BookListResponseRecord> findBookListByTagId(Long tagId);
}
