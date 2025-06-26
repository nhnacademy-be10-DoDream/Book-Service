package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.BookTag;
import shop.dodream.book.entity.BookTagId;
import shop.dodream.book.repository.querydsl.BookTagQuerydslRepository;

import java.util.Optional;

@Repository
public interface BookTagRepository extends JpaRepository<BookTag, BookTagId>, BookTagQuerydslRepository {
    boolean existsByBookIdAndTagId(Long bookId, Long tagId);
    Optional<BookTag> findByBookIdAndTagId(Long bookId, Long tagId);
}
