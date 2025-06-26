package shop.dodream.book.repository.querydsl;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookListResponse;

import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.QBook;
import shop.dodream.book.entity.QBookLike;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookLikeQuerydslRepositoryImpl implements BookLikeQuerydslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookListResponse> findLikedBooksByUserId(String userId) {
    QBookLike bookLike = QBookLike.bookLike;
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
            .from(bookLike)
            .join(bookLike.book, book)
            .where(bookLike.userId.eq(userId),
                    book.status.ne(BookStatus.REMOVED))
            .fetch();
    }
}
