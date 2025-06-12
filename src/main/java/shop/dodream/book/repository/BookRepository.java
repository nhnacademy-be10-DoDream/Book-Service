package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodream.book.dto.projection.BookListProjection;
import shop.dodream.book.dto.projection.UserBookDetailProjection;
import shop.dodream.book.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    // 모든 책을 프로젝션으로 조회
    List<BookListProjection> findAllBy();

    // 사용자가 사용하는 책 정보 단일조회 프로젝션으로 조회
    Optional<UserBookDetailProjection> findBookDetailForUserById(Long bookId);
}
