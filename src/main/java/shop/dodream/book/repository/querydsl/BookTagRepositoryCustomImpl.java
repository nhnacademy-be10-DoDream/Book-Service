package shop.dodream.book.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.QBookTag;
import shop.dodream.book.entity.Tag;

import java.util.List;

@RequiredArgsConstructor
public class BookTagRepositoryCustomImpl implements BookTagRepositoryCustom{
    private final JPAQueryFactory queryFactory; // 실제 쿼리를 만들어 실행하는 역할

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
    public List<Book> findAllByTagId(@Param("tagId") Long tagId) {
        QBookTag bookTag = QBookTag.bookTag;

        return queryFactory
                .select(bookTag.book)
                .from(bookTag)
                .where(bookTag.tag.id.eq(tagId))
                .fetch();
    }
}
