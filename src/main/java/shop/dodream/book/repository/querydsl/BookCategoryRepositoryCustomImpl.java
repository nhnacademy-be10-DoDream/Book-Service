package shop.dodream.book.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.QBookCategory;

import java.util.*;

@RequiredArgsConstructor
public class BookCategoryRepositoryCustomImpl implements BookCategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Set<Long> findCategoryIdsByBookId(@Param("bookId") Long bookId){
        QBookCategory bookCategory = QBookCategory.bookCategory;

        return new HashSet<>(
                queryFactory
                        .select(bookCategory.category.id)
                        .from(bookCategory)
                        .where(bookCategory.book.id.eq(bookId))
                        .fetch()
        );
    }

    @Override
    public List<Long> findBookIdsByCategoryId(@Param("categoryId") Long categoryId) {
        QBookCategory bookCategory = QBookCategory.bookCategory;
        return new ArrayList<>(
                queryFactory
                        .select(bookCategory.book.id)
                        .from(bookCategory)
                        .where(bookCategory.category.id.eq(categoryId))
                        .fetch()
        );
    }

    @Override
    public void deleteByBookIdAndCategoryIds(Long bookId, List<Long> categoryIds) {
        QBookCategory bookCategory = QBookCategory.bookCategory;

        queryFactory
                .delete(bookCategory)
                .where(
                        bookCategory.book.id.eq(bookId)
                                .and(bookCategory.category.id.in(categoryIds))
                )
                .execute();
    }

    @Override
    public List<Long> findExistingCategoryIds(@Param("bookId") Long bookId, @Param("categoryIds") List<Long> categoryIds){
        QBookCategory bookCategory = QBookCategory.bookCategory;

        return queryFactory
                .select(bookCategory.category.id)
                .from(bookCategory)
                .where(bookCategory.book.id.eq(bookId)
                    .and(bookCategory.category.id.in(categoryIds))
                )
                .fetch();
    }

    @Override
    public Optional<BookCategory> findExistingCategory(Long bookId, Long categoryId) {
        QBookCategory bookCategory = QBookCategory.bookCategory;

        BookCategory result = queryFactory
                .selectFrom(bookCategory)
                .where(
                        bookCategory.book.id.eq(bookId)
                                .and(bookCategory.category.id.eq(categoryId))
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
