package shop.dodream.book.repository.querydsl;
import org.springframework.data.repository.query.Param;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.BookCategory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookCategoryQuerydslRepository {
    Set<Long> findCategoryIdsByBookId(@Param("bookId") Long bookId);
    List<BookListResponseRecord> findBookListByCategoryId(Long categoryId);
    void deleteByBookIdAndCategoryIds(@Param("bookId") Long bookId, @Param("categoryIds") List<Long> categoryIds);
    List<Long> findExistingCategoryIds(@Param("bookId") Long bookId, @Param("categoryIds") List<Long> categoryIds);
    Optional<BookCategory> findExistingCategory(@Param("bookId") Long bookId, @Param("categoryId") Long categoryId);
}
