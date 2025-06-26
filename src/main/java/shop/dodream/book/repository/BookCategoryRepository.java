package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.BookCategoryId;
import shop.dodream.book.entity.Category;
import shop.dodream.book.repository.querydsl.BookCategoryRepositoryCustom;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryRepositoryCustom {
    boolean existsByBookIdAndCategoryId(Long bookId, Long categoryId);
}
