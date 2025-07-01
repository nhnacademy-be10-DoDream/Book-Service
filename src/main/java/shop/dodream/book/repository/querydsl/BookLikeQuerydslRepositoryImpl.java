package shop.dodream.book.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.dto.projection.QBookListResponseRecord;
import shop.dodream.book.entity.BookStatus;

import java.util.List;

import static com.querydsl.core.group.GroupBy.list;
import static shop.dodream.book.entity.QBook.book;
import static shop.dodream.book.entity.QBookLike.bookLike;
import static shop.dodream.book.entity.QImage.image;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookLikeQuerydslRepositoryImpl implements BookLikeQuerydslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookListResponseRecord> findLikedBooksByUserId(String userId) {

    return queryFactory.from(bookLike)
            .leftJoin(bookLike.book, book)
            .leftJoin(book.images, image).where(image.isThumbnail.eq(true))
            .where(
                    bookLike.userId.eq(userId),
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
}
