package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
