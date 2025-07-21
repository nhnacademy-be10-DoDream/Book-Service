package shop.dodream.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import shop.dodream.book.core.config.QuerydslConfig;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.repository.querydsl.BookCategoryQuerydslRepositoryImpl;
import shop.dodream.book.service.BookDocumentUpdater;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@Sql("/test-book-category.sql")
class BookCategoryQuerydslRepositoryTest {

    @Autowired
    private BookCategoryQuerydslRepositoryImpl repository;

    @MockBean
    private BookDocumentUpdater bookDocumentUpdater;

    @Test
    @DisplayName("카테고리 ID들로 도서 목록 페이징 조회")
    void findBookListByCategoryIds() {
        Set<Long> categoryIds = Set.of(1L, 3L);
        PageRequest pageable = PageRequest.of(0, 10);

        Page<BookListResponseRecord> page = repository.findBookListByCategoryIds(categoryIds, pageable);

        assertThat(page).isNotNull();
        assertThat(page.getContent()).isNotEmpty();
        for (BookListResponseRecord record : page.getContent()) {
            assertThat(record.title()).isNotBlank();
        }
    }

    @Test
    @DisplayName("책 ID로 등록된 카테고리 ID들 조회")
    void findCategoryIdsByBookId() {
        Long bookId = 1L;

        Set<Long> categoryIds = repository.findCategoryIdsByBookId(bookId);

        assertThat(categoryIds).isNotEmpty();
        assertThat(categoryIds).contains(1L, 4L);
    }

    @Test
    @DisplayName("책ID와 카테고리ID들로 기존 매핑 삭제")
    void deleteByBookIdAndCategoryIds() {
        Long bookId = 1L;
        List<Long> categoryIds = List.of(4L);

        repository.deleteByBookIdAndCategoryIds(bookId, categoryIds);

        Set<Long> remainingCategoryIds = repository.findCategoryIdsByBookId(bookId);
        assertThat(remainingCategoryIds).doesNotContain(4L);
    }

    @Test
    @DisplayName("책ID와 카테고리ID들 중 기존에 존재하는 카테고리 ID 조회")
    void findExistingCategoryIds() {
        Long bookId = 1L;
        List<Long> categoryIds = List.of(1L, 2L, 4L);

        List<Long> existingIds = repository.findExistingCategoryIds(bookId, categoryIds);

        assertThat(existingIds).contains(1L, 4L);
        assertThat(existingIds).doesNotContain(2L);
    }

    @Test
    @DisplayName("책ID와 카테고리ID로 기존 BookCategory 조회")
    void findExistingCategory() {
        Long bookId = 1L;
        Long categoryId = 1L;

        Optional<BookCategory> opt = repository.findExistingCategory(bookId, categoryId);

        assertThat(opt).isPresent();
        assertThat(opt.get().getBook().getId()).isEqualTo(bookId);
        assertThat(opt.get().getCategory().getId()).isEqualTo(categoryId);
    }
}

