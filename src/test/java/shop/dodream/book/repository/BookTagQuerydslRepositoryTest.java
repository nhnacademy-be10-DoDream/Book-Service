package shop.dodream.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import shop.dodream.book.core.config.QuerydslConfig;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.Tag;
import shop.dodream.book.repository.querydsl.BookTagQuerydslRepositoryImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@Sql("/test-book-tag.sql")
class BookTagQuerydslRepositoryTest {

    @Autowired
    private BookTagQuerydslRepositoryImpl bookTagQuerydslRepository;


    @Test
    @DisplayName("책 ID로 태그 전체 조회")
    void findAllByBookId() {
        Long bookId = 1L;

        List<Tag> tags = bookTagQuerydslRepository.findAllByBookId(bookId);

        assertThat(tags).isNotEmpty();
        assertThat(tags).extracting("tagName")
                .containsExactlyInAnyOrder("프로그래밍", "자바", "스프링");
    }

    @Test
    @DisplayName("태그 ID로 책 리스트 페이징 조회")
    void findBookListByTagId() {
        Long tagId = 1L;

        PageRequest pageable = PageRequest.of(0, 10);

        Page<BookListResponseRecord> page = bookTagQuerydslRepository.findBookListByTagId(tagId, pageable);

        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isGreaterThan(0);

        page.getContent().forEach(book -> {
            assertThat(book.bookId()).isNotNull();
            assertThat(book.title()).isNotBlank();
            assertThat(book.bookUrl()).isNotBlank();
        });
    }
}
