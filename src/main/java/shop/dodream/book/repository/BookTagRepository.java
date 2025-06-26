package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.BookTag;
import shop.dodream.book.entity.BookTagId;
import shop.dodream.book.entity.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookTagRepository extends JpaRepository<BookTag, BookTagId> {
    boolean existsByBookIdAndTagId(Long bookId, Long tagId);
    Optional<BookTag> findByBookIdAndTagId(Long bookId, Long tagId);

    @Query("SELECT bt.book.id FROM BookTag bt WHERE bt.tag.id = :tagId")
    List<Long> findBookIdsByTagId(@Param("tagId") Long tagId);

    List<Long> findByTagId(Long tagId);
}
