package shop.dodream.book.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.core.config.QuerydslConfig;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.dto.projection.ReviewSummaryResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.Review;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@Import(QuerydslConfig.class)
class ReviewRepositoryImplTest {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private TestEntityManager entityManager;

    private Book dummyBook;
    private Review dummyReview;
    private final String userId = "userId";

    @BeforeEach
    void setUp() {
        dummyBook = new Book(
                "title",
                "description",
                "author",
                "publisher",
                LocalDate.now(),
                "1234567898765",
                102L,
                BookStatus.SELL,
                1818L,
                true,
                0L,
                111L
        );
        dummyBook = entityManager.persistAndFlush(dummyBook);

        dummyReview = new Review(
                (short) 5,
                "홓핳힣헿!",
                userId,
                1818L,
                dummyBook
        );
        dummyReview = entityManager.persistAndFlush(dummyReview);
    }

    @Test
    void getByReviewId() {
        Optional<ReviewResponseRecord> result = reviewRepository.getByReviewId(dummyReview.getReviewId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(dummyReview.getReviewId(), result.get().reviewId());
        Assertions.assertEquals(dummyReview.getContent(), result.get().content());
        Assertions.assertEquals(userId, result.get().userId());
    }

    @Test
    void getByReviewId_NotFound() {
        Optional<ReviewResponseRecord> result = reviewRepository.getByReviewId(123L);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getByReviewIdAndUserId() {
        Optional<ReviewResponseRecord> result = reviewRepository.getByReviewIdAndUserId(dummyReview.getReviewId(), userId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(dummyReview.getReviewId(), result.get().reviewId());
        Assertions.assertEquals(userId, result.get().userId());
    }

    @Test
    void getByReviewIdAndUserId_NotFound() {
        Optional<ReviewResponseRecord> result = reviewRepository.getByReviewIdAndUserId(123L, userId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getAllBy() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewResponseRecord> result = reviewRepository.getAllBy(userId, pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(userId, result.getContent().getFirst().userId());
    }

    @Test
    void getAllBy_BlankUserId_NoFilterApplied() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ReviewResponseRecord> result = reviewRepository.getAllBy("", pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(userId, result.getContent().get(0).userId());
    }

    @Test
    void getAllBy_NoReviews() {
        String nonExistentUserId = "nonExistentUser";
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewResponseRecord> result = reviewRepository.getAllBy(nonExistentUserId, pageable);

        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllBy_EmptyReviewIds() {
        Pageable pageable = PageRequest.of(999, 10);
        Page<ReviewResponseRecord> result = reviewRepository.getAllBy(userId, pageable);

        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(1, result.getTotalElements());
    }


    @Test
    void getAllByBookId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewResponseRecord> result = reviewRepository.getAllByBookId(dummyBook.getId(), pageable);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(dummyBook.getId(), result.getContent().getFirst().bookId());
    }

    @Test
    void findReviewSummaryByBookId() {
        Optional<ReviewSummaryResponse> result = reviewRepository.findReviewSummaryByBookId(dummyBook.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(5.0, result.get().getRatingAvg());
        Assertions.assertEquals(1L, result.get().getReviewCount());
    }

    @Test
    void findReviewSummaryByBookId_NoReviews() {
        Optional<ReviewSummaryResponse> result = reviewRepository.findReviewSummaryByBookId(123L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(0.0, result.get().getRatingAvg());
        Assertions.assertEquals(0L, result.get().getReviewCount());
    }

    @Test
    void getByNoWriteReview() {
        List<Long> orderItemIds = List.of(1818L, 1819L);
        List<Long> result = reviewRepository.getByNoWriteReview(orderItemIds, userId);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1819L, result.getFirst());
        Assertions.assertFalse(result.contains(1818L));
    }

    @Test
    void getByNoWriteReview_Empty() {
        List<Long> result = reviewRepository.getByNoWriteReview(List.of(), userId);

        Assertions.assertEquals(0, result.size());
    }
}
