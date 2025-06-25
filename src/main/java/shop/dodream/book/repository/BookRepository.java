package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.dodream.book.entity.Book;
import shop.dodream.book.repository.querydsl.BookQuerydslRepository;


public interface BookRepository extends JpaRepository<Book, Long>, BookQuerydslRepository {


    boolean existsByIsbn(String isbn);

}
