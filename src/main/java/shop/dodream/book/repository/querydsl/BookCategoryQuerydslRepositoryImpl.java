package shop.dodream.book.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.dto.projection.QBookListResponseRecord;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.QBookCategory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.querydsl.core.group.GroupBy.list;
import static shop.dodream.book.entity.QBook.book;
import static shop.dodream.book.entity.QBookCategory.bookCategory;
import static shop.dodream.book.entity.QImage.image;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookCategoryQuerydslRepositoryImpl implements BookCategoryQuerydslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookListResponseRecord> findBookListByCategoryId(Long categoryId, Pageable pageable) {
        List<BookListResponseRecord> content = queryFactory
                .select(new QBookListResponseRecord(
                        book.id,
                        book.title,
                        book.author,
                        book.isbn,
                        book.regularPrice,
                        book.salePrice,
                        image.uuid
                ))
                .from(bookCategory)
                .leftJoin(bookCategory.book, book)
                .leftJoin(book.images, image).on(image.isThumbnail.eq(true))
                .where(
                        bookCategory.category.id.eq(categoryId),
                        book.status.ne(BookStatus.REMOVED)
                )
                .offset(pageable.getOffset()) // 페이징 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        Long total = queryFactory
                .select(book.count())
                .from(bookCategory)
                .leftJoin(bookCategory.book, book)
                .where(
                        bookCategory.category.id.eq(categoryId),
                        book.status.ne(BookStatus.REMOVED)
                )
                .fetchOne();

        long safeTotal = total != null ? total : 0L;

        return new PageImpl<>(content, pageable, safeTotal);
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
