package shop.dodream.book.repository.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.dto.projection.QBookListResponseRecord;
import shop.dodream.book.entity.BookStatus;

import java.util.List;

import static shop.dodream.book.entity.QBook.book;
import static shop.dodream.book.entity.QBookTag.bookTag;
import static shop.dodream.book.entity.QImage.image;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookTagQuerydslRepositoryImpl implements BookTagQuerydslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookListResponseRecord> findBookListByTagId(Long tagId) {
        return queryFactory.from(bookTag)
                .leftJoin(bookTag.book, book)
                .leftJoin(book.images, image).where(image.isThumbnail.eq(true))
                .where(
                        bookTag.tag.id.eq(tagId),
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
