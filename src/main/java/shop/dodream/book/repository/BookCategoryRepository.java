package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.BookCategoryId;
import shop.dodream.book.entity.Category;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {
    @Query("SELECT bc.category FROM BookCategory bc WHERE bc.book.id = :bookId")
    List<Category> findCategoriesByBook(@Param("bookId") Long bookId);

    @Query("SELECT bc.book FROM BookCategory bc WHERE bc.category.id = :categoryId")
    List<Book> findBooksByCategory(@Param("categoryId") Long categoryId);
}
