package shop.dodream.book.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import shop.dodream.book.core.config.QuerydslConfig;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookLike;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookLikeRepositoryTest {

    @Autowired
    private BookLikeRepository bookLikeRepository;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book(
                "Title", "Desc", "Author", "Publisher", LocalDate.now(),
                "isbn123", 10000L, BookStatus.SELL, 9000L, true, 0L, 10L
        );
        bookRepository.save(book);
    }

    @Test
    @DisplayName("existsByBookIdAndUserId 테스트")
    void existsByBookIdAndUserId() {
        bookLikeRepository.save(new BookLike("user1", book));

        boolean exists = bookLikeRepository.existsByBookIdAndUserId(book.getId(), "user1");
        boolean notExists = bookLikeRepository.existsByBookIdAndUserId(book.getId(), "user2");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("findByBookIdAndUserId 테스트")
    void findByBookIdAndUserId() {
        bookLikeRepository.save(new BookLike("user1", book));

        Optional<BookLike> found = bookLikeRepository.findByBookIdAndUserId(book.getId(), "user1");
        Optional<BookLike> notFound = bookLikeRepository.findByBookIdAndUserId(book.getId(), "user2");

        assertThat(found).isPresent();
        assertThat(found.get().getBook().getId()).isEqualTo(book.getId());
        assertThat(found.get().getUserId()).isEqualTo("user1");
        assertThat(notFound).isEmpty();
    }

    @Test
    @DisplayName("countByBookId 테스트")
    void countByBookId() {
        bookLikeRepository.save(new BookLike("user1", book));
        bookLikeRepository.save(new BookLike("user2", book));

        Long count = bookLikeRepository.countByBookId(book.getId());
        Long zeroCount = bookLikeRepository.countByBookId(999L);

        assertThat(count).isEqualTo(2L);
        assertThat(zeroCount).isEqualTo(0L);
    }

    @Test
    @DisplayName("findLikedBooksByUserId 테스트")
    void findLikedBooksByUserId() {
        bookLikeRepository.save(new BookLike("user1", book));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<?> result = bookLikeRepository.findLikedBooksByUserId("user1", pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).hasFieldOrPropertyWithValue("title", "Title");
    }
}
