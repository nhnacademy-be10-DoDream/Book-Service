package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.BookTag;
import shop.dodream.book.entity.BookTagId;

@Repository
public interface BookTagRepository extends JpaRepository<BookTag, BookTagId> {
}
