package shop.dodream.book.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.QBookResponse;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.dto.projection.QBookDetailResponse;
import shop.dodream.book.dto.projection.QBookListResponseRecord;
import shop.dodream.book.entity.BookStatus;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static shop.dodream.book.entity.QBook.book;
import static shop.dodream.book.entity.QImage.image;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookQuerydslRepositoryImpl implements BookQuerydslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookListResponseRecord> findAllBy(Pageable pageable) {
        List<BookListResponseRecord> content = queryFactory
                .select(new QBookListResponseRecord(
                        book.id,
                        book.title,
                        book.author,
                        book.isbn,
                        book.regularPrice,
                        book.salePrice,
                        image.uuid,
                        book.createdAt,
                        book.status
                ))
                .from(book)
                .leftJoin(book.images, image).on(image.isThumbnail.eq(true))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(book.createdAt.desc()) // 정렬 조건 필요 시
                .fetch();

        Long count = queryFactory
                .select(book.count())
                .from(book)
                .fetchOne();
        return new PageImpl<>(content, pageable, count);

    }

    @Override
    public List<BookListResponseRecord> findVisibleBooksByIds(List<Long> ids) {
        return queryFactory.from(book)
                .leftJoin(book.images, image).where(image.isThumbnail.eq(true))
                .where(
                        book.id.in(ids),
                        book.status.ne(BookStatus.REMOVED)
                )
                .select(new QBookListResponseRecord(
                        book.id,
                        book.title,
                        book.author,
                        book.isbn,
                        book.regularPrice,
                        book.salePrice,
                        image.uuid,
                        book.createdAt,
                        book.status
                ))
                .fetch();
    }


    @Override
    public Optional<BookDetailResponse> findBookDetailForUser(Long bookId) {
        return queryFactory.from(book)
                        .leftJoin(book.images, image)
                        .where(
                                book.id.eq(bookId),
                                book.status.ne(BookStatus.REMOVED)
                        )
                        .transform(
                                groupBy(book.id).list(
                                        new QBookDetailResponse(
                                                book.id,
                                                book.title,
                                                book.author,
                                                book.description,
                                                book.publisher,
                                                book.isbn,
                                                book.publishedAt,
                                                book.salePrice,
                                                book.regularPrice,
                                                book.isGiftable,
                                                list(image.uuid),
                                                book.discountRate
                                        )
                                )
                        ).stream()
                .findFirst();
    }

    @Override
    public Optional<BookDetailResponse> findBookDetailForAdmin(Long bookId) {
        return queryFactory.from(book)
                .leftJoin(book.images, image)
                .where(
                        book.id.eq(bookId)
                )
                .transform(
                        groupBy(book.id).list(
                                new QBookDetailResponse(
                                        book.id,
                                        book.title,
                                        book.author,
                                        book.description,
                                        book.publisher,
                                        book.isbn,
                                        book.publishedAt,
                                        book.salePrice,
                                        book.regularPrice,
                                        book.isGiftable,
                                        list(image.uuid),
                                        book.discountRate,
                                        book.status,
                                        book.createdAt,
                                        book.viewCount,
                                        book.bookCount
                                )
                        )
                ).stream()
                .findFirst();
    }

    @Override
    public Optional<BookResponse> findByIsbn(String isbn) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QBookResponse(book.id, book.title))
                        .from(book)
                        .where(book.isbn.eq(isbn))
                        .fetchOne()
        );
    }
}
