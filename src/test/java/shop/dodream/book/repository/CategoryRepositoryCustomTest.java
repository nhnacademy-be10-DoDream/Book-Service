package shop.dodream.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import shop.dodream.book.core.config.QuerydslConfig;
import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.dto.projection.CategoryWithParentProjection;
import shop.dodream.book.service.BookDocumentUpdater;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@Sql("/test-category.sql")
class CategoryRepositoryCustomTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private BookDocumentUpdater bookDocumentUpdater;

    @Test
    @DisplayName("모든 카테고리를 평면 리스트로 조회")
    void findAllFlat() {
        List<CategoryFlatProjection> categories = categoryRepository.findAllFlat();

        assertThat(categories).isNotEmpty();

        assertThat(categories)
                .extracting(CategoryFlatProjection::getCategoryName)
                .contains("소설", "경제", "IT", "문학", "소설_장르");

        assertThat(categories)
                .extracting(CategoryFlatProjection::getDepth)
                .allMatch(depth -> depth >= 1 && depth <= 3);

        categories.forEach(cat -> {
            if (cat.getDepth() == 1) {
                assertThat(cat.getParentId()).isNull();
            } else {
                assertThat(cat.getParentId()).isNotNull();
            }
        });
    }

    @Test
    @DisplayName("카테고리 ID로 카테고리와 부모 이름까지 조회")
    void findAllByIdsWithParent() {
        List<Long> categoryIds = List.of(1L, 2L);

        List<CategoryWithParentProjection> result = categoryRepository.findAllByIdsWithParent(categoryIds);

        assertThat(result).hasSize(2);
        for (CategoryWithParentProjection projection : result) {
            assertThat(projection.id()).isIn(categoryIds);
            assertThat(projection.categoryName()).isNotNull();
            assertThat(projection.depth()).isNotNull();

            if (projection.depth() > 1) {
                assertThat(projection.parentId()).isNotNull();
                assertThat(projection.parentName()).isNotBlank();
            }
        }
    }
}

