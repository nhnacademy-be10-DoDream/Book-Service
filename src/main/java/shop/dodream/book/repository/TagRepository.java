package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsById(Long tagId);
}
