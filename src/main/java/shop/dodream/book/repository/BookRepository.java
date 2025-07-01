package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.dodream.book.entity.Book;
import shop.dodream.book.repository.querydsl.BookQuerydslRepository;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long>, BookQuerydslRepository {
    boolean existsByIsbn(String isbn);
    boolean existsById(Long bookId);


}
