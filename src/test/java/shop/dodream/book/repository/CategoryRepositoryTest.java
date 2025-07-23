package shop.dodream.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import shop.dodream.book.core.config.QuerydslConfig;
import shop.dodream.book.entity.Category;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@Sql("/test-category.sql")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    @DisplayName("단계별 카테고리 조회")
    void findByDepth(){
        Long depth = 1L;

        List<Category> categories = categoryRepository.findByDepth(depth);

        assertThat(categories).isNotEmpty();
        assertThat(categories).allMatch(category -> category.getDepth().equals(depth));
    }

}
