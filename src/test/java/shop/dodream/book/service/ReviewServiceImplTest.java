package shop.dodream.book.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.event.PointEarnEvent;
import shop.dodream.book.core.event.ReviewImageDeleteEvent;
import shop.dodream.book.dto.PurchaseCheckResponse;
import shop.dodream.book.dto.ReviewCreateRequest;
import shop.dodream.book.dto.ReviewUpdateRequest;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.dto.projection.ReviewSummaryResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.Image;
import shop.dodream.book.entity.Review;
import shop.dodream.book.exception.BookNotFoundException;
import shop.dodream.book.exception.ReviewNotAllowedException;
import shop.dodream.book.exception.ReviewNotFoundException;
import shop.dodream.book.infra.client.OrderClient;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.ReviewRepository;
import shop.dodream.book.service.impl.FileServiceImpl;
import shop.dodream.book.service.impl.ReviewServiceImpl;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    @Mock
    private OrderClient orderClient;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private FileServiceImpl fileService;
    @InjectMocks
    private ReviewServiceImpl reviewService;

    private static final long BOOK_ID = 1L;
    private static final long REVIEW_ID = 1L;
    private static final String USER_ID = "userId";

    private ReviewCreateRequest createRequest;
    private ReviewUpdateRequest updateRequest;
    private Book mockBook;
    private Review mockReview;
    private MultipartFile mockFile;
    private Pageable pageable;
    private Review dummyReview;

    @BeforeEach
    void setUp() {
        createRequest = new ReviewCreateRequest((short) 1, "content");
        updateRequest = Mockito.mock(ReviewUpdateRequest.class);
        mockBook = Mockito.mock(Book.class);
        mockReview = Mockito.mock(Review.class);
        mockFile = Mockito.mock(MultipartFile.class);
        pageable = PageRequest.of(0, 10);
        dummyReview = new Review(
                (short) 4,
                "test test test!!!!!.",
                "userId",
                987L,
                mockBook
        );
    }

    @Nested
    class CreateReviewTests {

        void setupSuccessfulCreateReview() {
            Mockito.when(bookRepository.existsById(BOOK_ID)).thenReturn(true);
            Mockito.when(bookRepository.getReferenceById(BOOK_ID)).thenReturn(mockBook);
            Mockito.when(orderClient.checkOrderItem(USER_ID, BOOK_ID)).thenReturn(List.of(new PurchaseCheckResponse(10L)));
            Mockito.when(reviewRepository.getByNoWriteReview(Mockito.anyList(), Mockito.eq(USER_ID))).thenReturn(List.of(3L));
            Mockito.doNothing().when(eventPublisher).publishEvent(Mockito.any(PointEarnEvent.class));
        }

        @ParameterizedTest(name = "파일 있음={0}")
        @ValueSource(booleans = {false, true})
        void createReview_Success(boolean hasFiles) {
            setupSuccessfulCreateReview();
            List<MultipartFile> files = hasFiles ? List.of(mockFile) : List.of();
            List<String> uploadKeys = hasFiles ? List.of("fileKey") : List.of();

            Mockito.when(fileService.uploadReviewImageFromFiles(files)).thenReturn(uploadKeys);

            reviewService.createReview(BOOK_ID, USER_ID, createRequest, files);

            Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(Review.class));
            Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(PointEarnEvent.class));
        }

        @Test
        void createReview_SaveFails_ShouldDeleteImages() {
            setupSuccessfulCreateReview();
            Mockito.when(fileService.uploadReviewImageFromFiles(List.of(mockFile))).thenReturn(List.of("fileKey"));
            Mockito.doNothing().when(eventPublisher).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));
            Mockito.when(reviewRepository.save(Mockito.any())).thenThrow(DataAccessResourceFailureException.class);

            Assertions.assertThrows(Exception.class, () -> reviewService.createReview(BOOK_ID, USER_ID, createRequest, List.of(mockFile)));

            Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));
        }

        @Test
        void createReview_BookNotFound() {
            Mockito.when(bookRepository.existsById(BOOK_ID)).thenReturn(false);

            Assertions.assertThrows(BookNotFoundException.class, () -> reviewService.createReview(BOOK_ID, USER_ID, createRequest, List.of()));

            Mockito.verify(reviewRepository, Mockito.times(0)).save(Mockito.any());
        }

        @Test
        void createReview_NoPurchaseAllowed() {
            Mockito.when(bookRepository.existsById(BOOK_ID)).thenReturn(true);
            Mockito.when(orderClient.checkOrderItem(USER_ID, BOOK_ID)).thenReturn(List.of());
            Mockito.when(reviewRepository.getByNoWriteReview(Mockito.anyList(), Mockito.eq(USER_ID))).thenReturn(List.of());

            Assertions.assertThrows(ReviewNotAllowedException.class, () -> reviewService.createReview(BOOK_ID, USER_ID, createRequest, List.of()));

            Mockito.verify(reviewRepository, Mockito.times(0)).save(Mockito.any());
        }
    }

    @Nested
    class GetReviewsTests {

        @Test
        void getReviews_ByUserId() {
            Page<ReviewResponseRecord> expectedPage = Mockito.mock(Page.class);
            Mockito.when(reviewRepository.getAllBy(USER_ID, pageable)).thenReturn(expectedPage);

            Page<ReviewResponseRecord> result = reviewService.getReviews(USER_ID, pageable);

            Assertions.assertEquals(expectedPage, result);
            Mockito.verify(reviewRepository, Mockito.times(1)).getAllBy(USER_ID, pageable);
        }

        @Test
        void getReviewsByBookId() {
            Page<ReviewResponseRecord> expectedPage = Mockito.mock(Page.class);
            Mockito.when(reviewRepository.getAllByBookId(BOOK_ID, pageable)).thenReturn(expectedPage);

            Page<ReviewResponseRecord> result = reviewService.getReviewsByBookId(BOOK_ID, pageable);

            Assertions.assertEquals(expectedPage, result);
            Mockito.verify(reviewRepository, Mockito.times(1)).getAllByBookId(BOOK_ID, pageable);
        }

        @Test
        void getReview_ById_Success() {
            ReviewResponseRecord expectedRecord = Mockito.mock(ReviewResponseRecord.class);
            Mockito.when(reviewRepository.getByReviewId(REVIEW_ID)).thenReturn(Optional.of(expectedRecord));

            ReviewResponseRecord result = reviewService.getReview(REVIEW_ID);

            Assertions.assertEquals(expectedRecord, result);
            Mockito.verify(reviewRepository, Mockito.times(1)).getByReviewId(REVIEW_ID);
        }

        @Test
        void getReview_ById_NotFound() {
            Mockito.when(reviewRepository.getByReviewId(REVIEW_ID)).thenReturn(Optional.empty());

            Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(REVIEW_ID));
        }

        @Test
        void getReview_ByIdAndUserId_Success() {
            ReviewResponseRecord expectedRecord = Mockito.mock(ReviewResponseRecord.class);
            Mockito.when(reviewRepository.getByReviewIdAndUserId(REVIEW_ID, USER_ID)).thenReturn(Optional.of(expectedRecord));

            ReviewResponseRecord result = reviewService.getReview(REVIEW_ID, USER_ID);

            Assertions.assertEquals(expectedRecord, result);
            Mockito.verify(reviewRepository, Mockito.times(1)).getByReviewIdAndUserId(REVIEW_ID, USER_ID);
        }

        @Test
        void getReview_ByIdAndUserId_NotFound() {
            Mockito.when(reviewRepository.getByReviewIdAndUserId(REVIEW_ID, USER_ID)).thenReturn(Optional.empty());

            Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewService.getReview(REVIEW_ID, USER_ID));
        }

        @Test
        void getReviewSummary_ByBookId_Success() {
            ReviewSummaryResponse expectedRecord = Mockito.mock(ReviewSummaryResponse.class);
            Mockito.when(reviewRepository.findReviewSummaryByBookId(BOOK_ID)).thenReturn(Optional.of(expectedRecord));

            ReviewSummaryResponse result = reviewService.getReviewSummary(BOOK_ID);

            Assertions.assertEquals(expectedRecord, result);
            Mockito.verify(reviewRepository, Mockito.times(1)).findReviewSummaryByBookId(BOOK_ID);
        }

        @Test
        void getReviewSummary_ByBookId_Failed() {
            Mockito.when(reviewRepository.findReviewSummaryByBookId(BOOK_ID)).thenReturn(Optional.empty());

            Assertions.assertThrows(BookNotFoundException.class, () -> reviewService.getReviewSummary(BOOK_ID));

            Mockito.verify(reviewRepository, Mockito.times(1)).findReviewSummaryByBookId(BOOK_ID);
        }
    }

    @Nested
    class UpdateReviewTests {

        @Test
        void updateReview_ById_Success() {
            Mockito.when(reviewRepository.findWithImageByReviewId(REVIEW_ID)).thenReturn(Optional.of(dummyReview));
            Mockito.when(fileService.uploadReviewImageFromFiles(List.of(mockFile))).thenReturn(List.of("newKey"));
            Mockito.doNothing().when(eventPublisher).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));

            reviewService.updateReview(REVIEW_ID, updateRequest, List.of(mockFile));

            Mockito.verify(reviewRepository, Mockito.times(1)).findWithImageByReviewId(REVIEW_ID);
            Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));
        }

        @Test
        void updateReview_ByIdAndUserId_Success() {
            Mockito.when(reviewRepository.findWithImageByReviewIdAndUserId(REVIEW_ID, USER_ID)).thenReturn(Optional.of(dummyReview));
            Mockito.when(fileService.uploadReviewImageFromFiles(List.of(mockFile))).thenReturn(List.of());
            Mockito.doNothing().when(eventPublisher).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));
            Mockito.when(updateRequest.getImages()).thenReturn(null);

            reviewService.updateReview(REVIEW_ID, USER_ID, updateRequest, List.of(mockFile));

            Mockito.verify(reviewRepository, Mockito.times(1)).findWithImageByReviewIdAndUserId(REVIEW_ID, USER_ID);
            Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));
        }

    }

    @Nested
    class DeleteReviewTests {

        @Test
        void deleteReview_ById_Success() {
            Image mockImage = Mockito.mock(Image.class);
            Mockito.when(reviewRepository.findWithImageByReviewId(REVIEW_ID)).thenReturn(Optional.of(mockReview));
            Mockito.when(mockReview.getImages()).thenReturn(List.of(mockImage));
            Mockito.when(mockImage.getUuid()).thenReturn("imageKey");
            Mockito.doNothing().when(eventPublisher).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));

            reviewService.deleteReview(REVIEW_ID);

            Mockito.verify(reviewRepository, Mockito.times(1)).findWithImageByReviewId(REVIEW_ID);
            Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));
            Mockito.verify(reviewRepository, Mockito.times(1)).deleteById(REVIEW_ID);
        }

        @Test
        void deleteReview_ById_ReviewNotFound() {
            Mockito.when(reviewRepository.findWithImageByReviewId(REVIEW_ID)).thenReturn(Optional.empty());

            Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(REVIEW_ID));

            Mockito.verify(reviewRepository, Mockito.times(0)).deleteById(REVIEW_ID);
        }

        @Test
        void deleteReview_ByIdAndUserId_Success() {
            Image mockImage = Mockito.mock(Image.class);
            Mockito.when(reviewRepository.findWithImageByReviewIdAndUserId(REVIEW_ID, USER_ID)).thenReturn(Optional.of(mockReview));
            Mockito.when(mockReview.getImages()).thenReturn(List.of(mockImage));
            Mockito.when(mockImage.getUuid()).thenReturn("imageKey");
            Mockito.doNothing().when(eventPublisher).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));

            reviewService.deleteReview(REVIEW_ID, USER_ID);

            Mockito.verify(reviewRepository, Mockito.times(1)).findWithImageByReviewIdAndUserId(REVIEW_ID, USER_ID);
            Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(ReviewImageDeleteEvent.class));
            Mockito.verify(reviewRepository, Mockito.times(1)).deleteByReviewIdAndUserId(REVIEW_ID, USER_ID);
        }

        @Test
        void deleteReview_ByIdAndUserId_ReviewNotFound() {
            Mockito.when(reviewRepository.findWithImageByReviewIdAndUserId(REVIEW_ID, USER_ID)).thenReturn(Optional.empty());

            Assertions.assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(REVIEW_ID, USER_ID));

            Mockito.verify(reviewRepository, Mockito.times(0)).deleteByReviewIdAndUserId(REVIEW_ID, USER_ID);
        }
    }
}
