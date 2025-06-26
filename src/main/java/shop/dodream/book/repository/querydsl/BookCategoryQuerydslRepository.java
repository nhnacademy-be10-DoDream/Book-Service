package shop.dodream.book.repository.querydsl;

import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;

public interface BookCategoryQuerydslRepository {
    List<BookListResponseRecord> findBookListByCategoryId(Long categoryId);
}
