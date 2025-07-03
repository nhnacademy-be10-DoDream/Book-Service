package shop.dodream.book.service;

import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.dto.ReviewCreateRequest;
import shop.dodream.book.dto.ReviewUpdateRequest;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.dto.projection.ReviewSummaryResponse;

import java.util.List;

public interface ReviewService {
    void createReview(Long bookId, String userId, ReviewCreateRequest reviewCreateRequest, List<MultipartFile> files);

    List<ReviewResponseRecord> getReviews(String userId);

    List<ReviewResponseRecord> getReviewsByUserId(String userId);

    List<ReviewResponseRecord> getReviewsByBookId(Long bookId);

    ReviewResponseRecord getReview(Long reviewId);

    ReviewResponseRecord getReview(Long reviewId, String userId);

    void updateReview(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, List<MultipartFile> files);

    void updateReview(Long reviewId, String userId, ReviewUpdateRequest reviewUpdateRequest, List<MultipartFile> files);

    void deleteReview(Long reviewId);

    void deleteReview(Long reviewId, String userId);

    ReviewSummaryResponse getReviewSummary(Long bookId);
}
