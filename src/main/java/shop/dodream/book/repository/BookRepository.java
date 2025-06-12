package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;

import java.util.Arrays;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
//    List<Book> findByStatusNot(BookStatus bookStatus);
}
