package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookTag;
import shop.dodream.book.entity.BookTagId;
import shop.dodream.book.entity.Tag;

import java.util.List;

@Repository
public interface BookTagRepository extends JpaRepository<BookTag, BookTagId> {
    @Query("SELECT bt.tag FROM BookTag bt WHERE bt.book.id = :bookId")
    List<Tag> findTagsByBook(@Param("bookId") Long bookId);

    @Query("SELECT bt.book FROM BookTag bt WHERE bt.tag.id = :tagId")
    List<Book> findBooksByTag(@Param("tagId") Long tagId);
}
