package shop.dodream.book.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.dto.UserBookDetailResponse;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.QBook;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookQuerydslRepositoryImpl implements BookQuerydslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookListResponse> findAllBy() {
        QBook book = QBook.book;

        return queryFactory
                .select(Projections.constructor(
                        BookListResponse.class,
                        book.id,
                        book.title,
                        book.author,
                        book.isbn,
                        book.regularPrice,
                        book.salePrice,
                        book.bookUrl
                ))
                .from(book)
                .fetch();
    }


    @Override
    public Optional<UserBookDetailResponse> findBookDetailForUserById(Long bookId) {
        QBook book = QBook.book;

        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(
                                UserBookDetailResponse.class,
                                book.title,
                                book.author,
                                book.description,
                                book.publisher,
                                book.isbn,
                                book.publishedAt,
                                book.salePrice,
                                book.regularPrice,
                                book.isGiftable,
                                book.bookUrl,
                                book.discountRate
                        ))
                        .from(book)
                        .where(
                                book.id.eq(bookId),
                                book.status.ne(BookStatus.REMOVED)
                        )
                        .fetchOne()
        );

    }

}
