package shop.dodream.book.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.dto.projection.QBookListResponseRecord;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.QBookCategory;

import java.util.*;

import static shop.dodream.book.entity.QBook.book;
import static shop.dodream.book.entity.QBookCategory.bookCategory;
import static shop.dodream.book.entity.QImage.image;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookCategoryQuerydslRepositoryImpl implements BookCategoryQuerydslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookListResponseRecord> findBookListByCategoryId(Long categoryId) {
        return queryFactory.from(bookCategory)
                .leftJoin(bookCategory.book, book)
                .leftJoin(book.images, image).where(image.isThumbnail.eq(true))
                .where(
                        bookCategory.category.id.eq(categoryId),
                        book.status.ne(BookStatus.REMOVED)
                )
                .select(new QBookListResponseRecord(
                        book.id,
                        book.title,
                        book.author,
                        book.isbn,
                        book.regularPrice,
                        book.salePrice,
                        image.uuid
                ))
                .fetch();
    }

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
