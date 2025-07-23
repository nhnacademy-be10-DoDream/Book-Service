package shop.dodream.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import shop.dodream.book.core.config.QuerydslConfig;
import shop.dodream.book.entity.BookCategory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@Sql("/test-book-category.sql")
class BookCategoryRepositoryTest {

    @Autowired
    private BookCategoryRepository bookCategoryRepository;



    @Test
    @DisplayName("책ID와 카테고리ID로 존재 여부 확인")
    void existsByBookIdAndCategoryId() {
        boolean exists = bookCategoryRepository.existsByBookIdAndCategoryId(1L, 1L);
        assertThat(exists).isTrue();

        boolean notExists = bookCategoryRepository.existsByBookIdAndCategoryId(999L, 999L);
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("책ID로 관련 카테고리 목록 조회")
    void findByBookId() {
        List<BookCategory> bookCategories = bookCategoryRepository.findByBookId(1L);

        assertThat(bookCategories).isNotEmpty();
        assertThat(bookCategories).allMatch(bc -> bc.getBook().getId().equals(1L));
    }
}
