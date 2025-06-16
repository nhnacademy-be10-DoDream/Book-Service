package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.Category;
import shop.dodream.book.repository.querydsl.CategoryRepositoryCustom;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
    List<Category> findByDepth(Long depth);
}
