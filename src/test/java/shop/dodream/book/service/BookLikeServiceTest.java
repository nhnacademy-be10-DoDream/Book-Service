package shop.dodream.book.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookLike;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.exception.BookLikeNotFoundException;
import shop.dodream.book.repository.BookLikeRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.impl.BookLikeServiceImpl;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookLikeServiceTest {

    @InjectMocks
    private BookLikeServiceImpl bookLikeService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookLikeRepository bookLikeRepository;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book(
                "제목", "설명", "저자", "출판사",
                LocalDate.of(2023, 1, 1),
                "1234567890123",
                10000L,
                BookStatus.SELL,
                9000L,
                true,
                0L,
                100L
        );
        ReflectionTestUtils.setField(sampleBook, "id", 1L);
    }

    @Test
    @DisplayName("registerBookLike - 북 좋아요 등록 성공")
    void registerBookLike_success() {
        // given
        when(bookLikeRepository.existsByBookIdAndUserId(1L, "user1")).thenReturn(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        // when
        bookLikeService.registerBookLike(1L, "user1");

        // then
        verify(bookLikeRepository).save(any(BookLike.class));
    }

    @Test
    @DisplayName("bookLikeFindMe - 내가 좋아요 눌렀는지 확인")
    void bookLikeFindMe_success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookLikeRepository.existsByBookIdAndUserId(1L, "user1")).thenReturn(true);

        Boolean liked = bookLikeService.bookLikeFindMe(1L, "user1");

        assertTrue(liked);
    }

    @Test
    @DisplayName("bookLikeDelete - 북 좋아요 취소 성공")
    void bookLikeDelete_success() {
        BookLike bookLike = new BookLike("user1", sampleBook);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookLikeRepository.findByBookIdAndUserId(1L, "user1")).thenReturn(Optional.of(bookLike));


        // when
        bookLikeService.bookLikeDelete(1L, "user1");

        // then
        verify(bookLikeRepository).delete(bookLike);
    }

    @Test
    @DisplayName("bookLikeDelete - 북 좋아요 취소 실패 (존재하지 않음)")
    void bookLikeDelete_fail_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(bookLikeRepository.findByBookIdAndUserId(1L, "user1")).thenReturn(Optional.empty());

        assertThrows(BookLikeNotFoundException.class, () -> {
            bookLikeService.bookLikeDelete(1L, "user1");
        });
    }

    @Test
    @DisplayName("getLikedBooksByUserId - 사용자가 누른 좋아요 목록 조회")
    void getLikedBooksByUserId_success() {
        // given
        PageRequest pageable = PageRequest.of(0, 10);
        BookListResponseRecord record = new BookListResponseRecord(
                1L,
                "제목",
                "저자",
                "1234567890123",
                10000L,
                9000L,
                "cover.jpg"
        );
        Page<BookListResponseRecord> page = new PageImpl<>(List.of(record), pageable, 1);
        when(bookLikeRepository.findLikedBooksByUserId("user1", pageable)).thenReturn(page);

        // when
        Page<BookListResponseRecord> result = bookLikeService.getLikedBooksByUserId("user1", pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("제목", result.getContent().getFirst().title());
    }

    @Test
    @DisplayName("getBookLikeCount - 특정 도서의 좋아요 수 조회")
    void getBookLikeCount_success() {
        // given
        when(bookLikeRepository.countByBookId(1L)).thenReturn(42L);

        // when
        Long count = bookLikeService.getBookLikeCount(1L);

        // then
        assertEquals(42L, count);
    }
}