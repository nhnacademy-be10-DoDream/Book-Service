package shop.dodream.book.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.core.config.QuerydslConfig;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.projection.BookAdminListResponseRecord;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.Image;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {


    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;



    @Test
    @DisplayName("existsByIsbn 테스트")
    void existsByIsbnTest() {
        // given
        String isbn = "1234567890123";
        Book book = new Book(
                "Test Title",
                "Test Description",
                "Test Author",
                "Test Publisher",
                LocalDate.now(),
                isbn,
                20000L,
                BookStatus.SELL,
                18000L,
                true,
                0L,
                100L
        );

        bookRepository.save(book);

        // when
        boolean exists = bookRepository.existsByIsbn(isbn);
        boolean notExists = bookRepository.existsByIsbn("9999999999999");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("existsById 테스트")
    void existsByIdTest() {
        // given
        Book book = new Book(
                "Another Title",
                "Another Description",
                "Another Author",
                "Another Publisher",
                LocalDate.now(),
                "9876543210987",
                25000L,
                BookStatus.SELL,
                20000L,
                false,
                0L,
                50L
        );
        Book saved = bookRepository.save(book);

        // when
        boolean exists = bookRepository.existsById(saved.getId());
        boolean notExists = bookRepository.existsById(9999L);

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }



    @Test
    @DisplayName("findAllBy(Pageable) 테스트")
    void findAllByPageableTest(){
        Book book1 = new Book("Title1", "Desc1", "Author1", "Publisher1", LocalDate.now(), "isbn1", 10000L, BookStatus.SELL, 9000L, true, 0L, 10L);
        Book book2 = new Book("Title2", "Desc2", "Author2", "Publisher2", LocalDate.now(), "isbn2", 20000L, BookStatus.SELL, 18000L, true, 0L, 20L);

        bookRepository.save(book1);
        bookRepository.save(book2);


        Pageable pageable = PageRequest.of(0, 10);

        Page<BookAdminListResponseRecord> result = bookRepository.findAllBy(pageable);

        assertThat(result).hasSize(2);

    }

    @Test
    @DisplayName("findAllBy 테스트")
    void findAllByTest(){
        Book book1 = new Book("Title1", "Desc1", "Author1", "Publisher1", LocalDate.now(), "isbn1", 10000L, BookStatus.SELL, 9000L, true, 0L, 10L);
        Book book2 = new Book("Title2", "Desc2", "Author2", "Publisher2", LocalDate.now(), "isbn2", 20000L, BookStatus.REMOVED, 18000L, true, 0L, 20L);


        bookRepository.save(book1);
        bookRepository.save(book2);


        List<BookListResponseRecord> result = bookRepository.findAllBy();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().title()).isEqualTo("Title1");
    }

    @Test
    @DisplayName("findVisibleBooksByIds 테스트")
    void findVisibleBooksByIds(){
        Book visibleBook = new Book("Title1", "Desc1", "Author1", "Publisher1", LocalDate.now(), "isbn1", 10000L, BookStatus.SELL, 9000L, true, 0L, 10L);
        Book removedBook = new Book("Title2", "Desc2", "Author2", "Publisher2", LocalDate.now(), "isbn2", 20000L, BookStatus.REMOVED, 18000L, true, 0L, 20L);

        Image image = new Image(visibleBook, "uuid1", true);

        visibleBook.addImages(List.of(image));

        bookRepository.save(visibleBook);
        bookRepository.save(removedBook);


        List<Long> ids = List.of(visibleBook.getId(), removedBook.getId());

        List<BookListResponseRecord> result = bookRepository.findVisibleBooksByIds(ids);

        assertThat(result).hasSize(1);
        BookListResponseRecord record = result.getFirst();
        assertThat(record.bookId()).isEqualTo(visibleBook.getId());
        assertThat(record.title()).isEqualTo("Title1");
        assertThat(record.isbn()).isEqualTo("isbn1");
        assertThat(record.bookUrl()).isEqualTo("uuid1");

    }

    @Test
    @DisplayName("findBookDetailForUser 테스트")
    void findBookDetailForUser() {
        Book book = new Book(
                "Title1", "Desc1", "Author1", "Publisher1", LocalDate.now(),
                "isbn1", 10000L, BookStatus.SELL, 9000L, true, 0L, 10L
        );
        Image image1 = new Image(book, "uuid1", true);
        Image image2 = new Image(book, "uuid2", false);

        book.addImages(List.of(image1, image2));

        bookRepository.save(book);


        // when
        Optional<BookDetailResponse> optional = bookRepository.findBookDetailForUser(book.getId());

        // then
        assertThat(optional).isPresent();
        BookDetailResponse detail = optional.get();

        assertThat(detail.getBookId()).isEqualTo(book.getId());
        assertThat(detail.getTitle()).isEqualTo("Title1");
        assertThat(detail.getIsbn()).isEqualTo("isbn1");
        assertThat(detail.getBookUrls()).containsExactlyInAnyOrder("uuid1", "uuid2");
    }

    @Test
    @DisplayName("findBookDetailForAdmin 테스트")
    void findBookDetailForAdmin(){
        Book book = new Book(
                "Title1", "Desc1", "Author1", "Publisher1", LocalDate.now(),
                "isbn1", 10000L, BookStatus.SELL, 9000L, true, 0L, 10L
        );
        Image image1 = new Image(book, "uuid1", true);
        Image image2 = new Image(book, "uuid2", false);

        book.addImages(List.of(image1, image2));

        bookRepository.save(book);


        Optional<BookDetailResponse> optional = bookRepository.findBookDetailForAdmin(book.getId());

        assertThat(optional).isPresent();
        BookDetailResponse detail = optional.get();

        assertThat(detail.getBookId()).isEqualTo(book.getId());
        assertThat(detail.getTitle()).isEqualTo("Title1");
        assertThat(detail.getIsbn()).isEqualTo("isbn1");
        assertThat(detail.getStatus()).isEqualTo(BookStatus.SELL);
        assertThat(detail.getViewCount()).isEqualTo(0L);
        assertThat(detail.getBookCount()).isEqualTo(10L);
        assertThat(detail.getBookUrls()).containsExactlyInAnyOrder("uuid1", "uuid2");
        assertThat(detail.getCreatedAt()).isNotNull();

    }

    @Test
    @DisplayName("findByIsbn 테스트")
    void findByIsbn(){
        Book book = new Book(
                "Title1", "Desc1", "Author1", "Publisher1", LocalDate.now(),
                "isbn1", 10000L, BookStatus.SELL, 9000L, true, 0L, 10L
        );

        bookRepository.save(book);


        Optional<BookItemResponse> result = bookRepository.findByIsbn("isbn1");

        assertThat(result).isPresent();
        BookItemResponse response = result.get();
        assertThat(response.getBookId()).isEqualTo(book.getId());
        assertThat(response.getTitle()).isEqualTo(book.getTitle());

        Optional<BookItemResponse> notFound = bookRepository.findByIsbn("nonexistent-isbn");
        assertThat(notFound).isEmpty();
    }

    @Test
    @DisplayName("incrementViewCount 테스트")
    void incrementViewCount(){
        Book book = new Book(
                "Title1", "Desc1", "Author1", "Publisher1", LocalDate.now(),
                "isbn1", 10000L, BookStatus.SELL, 9000L, true, 0L, 10L
        );

        bookRepository.save(book);


        bookRepository.incrementViewCount(book.getId(), 3L);

        entityManager.flush();
        entityManager.clear();

        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(updatedBook.getViewCount()).isEqualTo(3L);
    }





}
