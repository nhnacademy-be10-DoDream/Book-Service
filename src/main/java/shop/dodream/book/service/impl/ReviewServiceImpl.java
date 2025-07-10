package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
import shop.dodream.book.service.BookDocumentUpdater;
import shop.dodream.book.service.FileService;
import shop.dodream.book.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final OrderClient orderClient;
    private final ApplicationEventPublisher eventPublisher;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final FileService fileService;
    private final BookDocumentUpdater bookDocumentUpdater;

    @Transactional
    public void createReview(Long bookId, String userId, ReviewCreateRequest reviewCreateRequest, List<MultipartFile> files) {

        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        List<PurchaseCheckResponse> checkResponses = orderClient.checkOrderItem(userId, bookId);

        List<Long> noWriteReviewByItems = reviewRepository.getByNoWriteReview(checkResponses.stream()
                .map(PurchaseCheckResponse::orderItemId).toList(), userId);

        if (noWriteReviewByItems.isEmpty()) {
            throw new ReviewNotAllowedException(userId);
        }

        Book book = bookRepository.getReferenceById(bookId);
        Review review = reviewCreateRequest.toEntity(book, userId, noWriteReviewByItems.getFirst());

        List<String> uploadedImageKeys = fileService.uploadReviewImageFromFiles(files);

        try {
            review.addImages(createReviewImages(review, uploadedImageKeys));
            reviewRepository.save(review);
            bookDocumentUpdater.increaseReviewStatus(bookId, reviewCreateRequest.getRating());
        }catch (Exception e) {
            eventPublisher.publishEvent(new ReviewImageDeleteEvent(uploadedImageKeys));
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseRecord> getReviews(String userId, Pageable pageable) {

        return reviewRepository.getAllBy(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseRecord> getReviewsByBookId(Long bookId, Pageable pageable) {
        return reviewRepository.getAllByBookId(bookId, pageable);
    }

    @Transactional(readOnly = true)
    public ReviewResponseRecord getReview(Long reviewId) {
        return reviewRepository.getByReviewId(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException(reviewId));
    }

    @Transactional(readOnly = true)
    public ReviewResponseRecord getReview(Long reviewId, String userId) {
        return reviewRepository.getByReviewIdAndUserId(reviewId, userId)
                .orElseThrow(()-> new ReviewNotFoundException(reviewId));
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, List<MultipartFile> files) {
        Review review = findWithImageByReviewId(reviewId);
        List<String> uploadedKeys = fileService.uploadReviewImageFromFiles(files);

        List<String> deleteKeys = review.update(reviewUpdateRequest);
        eventPublisher.publishEvent(new ReviewImageDeleteEvent(deleteKeys));

        review.addImages(createReviewImages(review, uploadedKeys));
    }

    @Transactional
    public void updateReview(Long reviewId, String userId, ReviewUpdateRequest reviewUpdateRequest, List<MultipartFile> files) {
        Review review = findWithImageByReviewIdAndUserId(reviewId, userId);
        List<String> uploadedKeys = fileService.uploadReviewImageFromFiles(files);

        List<String> deleteKeys = review.update(reviewUpdateRequest);
        eventPublisher.publishEvent(new ReviewImageDeleteEvent(deleteKeys));

        review.addImages(createReviewImages(review, uploadedKeys));

        bookDocumentUpdater.updateReviewStatus(review.getBook().getId(), review.getRating(), reviewUpdateRequest.getRating());

    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = findWithImageByReviewId(reviewId);

        List<String> deleteKeys = review.getImages().stream()
                .map(Image::getUuid)
                .toList();

        eventPublisher.publishEvent(new ReviewImageDeleteEvent(deleteKeys));

        reviewRepository.deleteById(reviewId);

        bookDocumentUpdater.decreaseReviewStatus(review.getBook().getId(), review.getRating());
    }

    @Transactional
    public void deleteReview(Long reviewId, String userId) {
        Review review = findWithImageByReviewIdAndUserId(reviewId, userId);

        List<String> deleteKeys = review.getImages().stream()
                .map(Image::getUuid)
                .toList();

        eventPublisher.publishEvent(new ReviewImageDeleteEvent(deleteKeys));

        reviewRepository.deleteByReviewIdAndUserId(reviewId, userId);

    }

    private Review findWithImageByReviewId(Long reviewId) {
        return reviewRepository.findWithImageByReviewId(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException(reviewId));
    }

    private Review findWithImageByReviewIdAndUserId(Long reviewId, String userId) {
        return reviewRepository.findWithImageByReviewIdAndUserId(reviewId, userId)
                .orElseThrow(()-> new ReviewNotFoundException(reviewId));
    }

    private List<Image> createReviewImages(Review review, List<String> imageUrls) {
        List<Image> reviewImages = new ArrayList<>(imageUrls.size());

        for (String imageUrl : imageUrls) {
            Image reviewImage = new Image(review, imageUrl);
            reviewImages.add(reviewImage);
        }

        return reviewImages;
    }

    @Override
    public ReviewSummaryResponse getReviewSummary(Long bookId) {

        return reviewRepository.findReviewSummaryByBookId(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

    }
}