package shop.dodream.book.repository.querydsl;


import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.dto.projection.ReviewSummaryResponse;

import java.util.List;
import java.util.Optional;

public interface ReviewQuerydslRepository {
    Optional<ReviewResponseRecord> getByReviewId(long reviewId);
    Optional<ReviewResponseRecord> getByReviewIdAndUserId(long reviewId, String userId);
    List<ReviewResponseRecord> getAllBy(String userId);
    List<ReviewResponseRecord> getAllByUserId(String userId);
    List<ReviewResponseRecord> getAllByBookId(long bookId);

    Optional<ReviewSummaryResponse> findReviewSummaryByBookId(long bookId);
}