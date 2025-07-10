package shop.dodream.book.repository.querydsl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.dto.projection.ReviewSummaryResponse;

import java.util.List;
import java.util.Optional;

public interface ReviewQuerydslRepository {
    Optional<ReviewResponseRecord> getByReviewId(long reviewId);
    Optional<ReviewResponseRecord> getByReviewIdAndUserId(long reviewId, String userId);
    Page<ReviewResponseRecord> getAllBy(String userId, Pageable pageable);
    Page<ReviewResponseRecord> getAllByBookId(long bookId, Pageable pageable);
    Optional<ReviewSummaryResponse> findReviewSummaryByBookId(long bookId);
    List<Long> getByNoWriteReview(List<Long> orderItemIds, String userId);
}