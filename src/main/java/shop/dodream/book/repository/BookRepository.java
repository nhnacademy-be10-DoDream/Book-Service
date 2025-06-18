package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.dodream.book.entity.Book;



public interface BookRepository extends JpaRepository<Book, Long>, BookQuerydslRepository {


    boolean existsByIsbn(String isbn);

}
