package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.BookCategoryId;
import shop.dodream.book.repository.querydsl.BookCategoryQuerydslRepository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryQuerydslRepository {
    boolean existsByBookIdAndCategoryId(Long bookId, Long categoryId);
    List<BookCategory> findByBookId(Long bookId);
}
