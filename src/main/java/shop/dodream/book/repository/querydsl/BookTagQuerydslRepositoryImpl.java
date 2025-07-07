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
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.QBookTag;
import shop.dodream.book.entity.Tag;

import java.util.List;

import static com.querydsl.core.group.GroupBy.list;
import static shop.dodream.book.entity.QBook.book;
import static shop.dodream.book.entity.QBookTag.bookTag;
import static shop.dodream.book.entity.QImage.image;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookTagQuerydslRepositoryImpl implements BookTagQuerydslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tag> findAllByBookId(Long bookId) {
        QBookTag bookTag = QBookTag.bookTag;

        return queryFactory
                .select(bookTag.tag)
                .from(bookTag)
                .where(bookTag.book.id.eq(bookId))
                .fetch();
    }

    @Override
    public Page<BookListResponseRecord> findBookListByTagId(Long tagId, Pageable pageable) {
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
                .from(bookTag)
                .leftJoin(bookTag.book, book)
                .leftJoin(book.images, image).on(image.isThumbnail.eq(true))
                .where(
                        bookTag.tag.id.eq(tagId),
                        book.status.ne(BookStatus.REMOVED)
                )
                .offset(pageable.getOffset()) // 페이지 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        Long total = queryFactory
                .select(book.id.count())
                .from(bookTag)
                .leftJoin(bookTag.book, book)
                .where(
                        bookTag.tag.id.eq(tagId),
                        book.status.ne(BookStatus.REMOVED)
                )
                .fetchOne();
        long safeTotal = total != null ? total : 0L;
        return new PageImpl<>(content, pageable, safeTotal);
    }

}
