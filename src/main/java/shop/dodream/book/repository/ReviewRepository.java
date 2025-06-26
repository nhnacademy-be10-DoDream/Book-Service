package shop.dodream.book.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.dodream.book.entity.Review;
import shop.dodream.book.repository.querydsl.ReviewQuerydslRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQuerydslRepository {
    @EntityGraph(attributePaths = "images")
    Optional<Review> findWithImageByReviewId(long reviewId);

    @EntityGraph(attributePaths = "images")
    Optional<Review> findWithImageByReviewIdAndUserId(long reviewId, String userId);

    @EntityGraph(attributePaths = {"images", "user"})
    Optional<Review> findWithImageAndUserByReviewId(long reviewId);

    void deleteByReviewIdAndUserId(long reviewId, String userId);

}