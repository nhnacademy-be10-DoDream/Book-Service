package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.BookCategoryId;
import shop.dodream.book.repository.querydsl.BookCategoryQuerydslRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryQuerydslRepository {
    @Query("SELECT bc.category.id FROM BookCategory bc WHERE bc.book.id = :bookId")
    Set<Long> findCategoryIdsByBookId(@Param("bookId") Long bookId);

    @Modifying
    @Query("DELETE FROM BookCategory bc WHERE bc.book.id = :bookId AND bc.category.id IN :categoryIds")
    void deleteByBookIdAndCategoryIds(@Param("bookId") Long bookId, @Param("categoryIds") List<Long> categoryIds);

    @Query("SELECT bc.category.id FROM BookCategory bc WHERE bc.book.id = :bookId AND bc.category.id IN :categoryIds")
    List<Long> findExistingCategoryIds(@Param("bookId") Long bookId, @Param("categoryIds") List<Long> categoryIds);

    @Query("SELECT bc FROM BookCategory bc WHERE bc.book.id = :bookId AND bc.category.id = :categoryId")
    Optional<BookCategory> findExistingCategoryId(@Param("bookId") Long bookId, @Param("categoryId") Long categoryId);

    boolean existsByBookIdAndCategoryId(Long bookId, Long categoryId);
}
