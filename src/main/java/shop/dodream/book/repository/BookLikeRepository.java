package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodream.book.entity.BookLike;

import java.util.Optional;

public interface BookLikeRepository extends JpaRepository<BookLike, Long>, BookLikeQuerydslRepository {


    boolean existsByBookIdAndUserId(Long bookId, String userId);

    Optional<BookLike> findByBookIdAndUserId(Long bookId, String userId);

    Long countByBookId(Long bookId);




}
