package shop.dodream.book.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.dto.projection.QBookListResponseRecord;
import shop.dodream.book.dto.projection.QReviewResponseRecord;
import shop.dodream.book.entity.BookStatus;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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
    public Page<BookListResponseRecord> findLikedBooksByUserId(String userId, Pageable pageable) {

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
                .from(bookLike)
                .leftJoin(bookLike.book, book)
                .leftJoin(book.images, image).on(image.isThumbnail.eq(true))
                .where(
                        bookLike.userId.eq(userId),
                        book.status.ne(BookStatus.REMOVED)
                )
                .orderBy(bookLike.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(queryFactory
                .select(bookLike.count())
                .from(bookLike)
                .leftJoin(bookLike.book, book)
                .where(
                        bookLike.userId.eq(userId),
                        book.status.ne(BookStatus.REMOVED)
                )
                .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}
