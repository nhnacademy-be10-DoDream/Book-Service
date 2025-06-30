package shop.dodream.book.repository.querydsl;

import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.dodream.book.dto.projection.QReviewResponseRecord;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.entity.Review;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static shop.dodream.book.entity.QImage.image;
import static shop.dodream.book.entity.QReview.review;


public class ReviewQuerydslRepositoryImpl extends QuerydslRepositorySupport implements ReviewQuerydslRepository {
    private final JPQLQueryFactory queryFactory;

    public ReviewQuerydslRepositoryImpl(JPQLQueryFactory queryFactory) {
        super(Review.class);
        this.queryFactory = queryFactory;
    }


    @Override
    public Optional<ReviewResponseRecord> getByReviewId(long reviewId) {
        return queryFactory.from(review)
                .leftJoin(review.images, image)
                .where(review.reviewId.eq(reviewId))
                .transform(
                        groupBy(review.reviewId).list(
                                new QReviewResponseRecord(
                                        review.reviewId,
                                        review.rating,
                                        review.content,
                                        review.createdAt,
                                        review.book.id,
                                        list(image.uuid),
                                        review.userId
                                )
                        )
                ).stream()
                .findFirst();
    }

    @Override
    public Optional<ReviewResponseRecord> getByReviewIdAndUserId(long reviewId, String userId) {
        return queryFactory.from(review)
                .leftJoin(review.images, image)
                .where(review.reviewId.eq(reviewId)
                        .and(review.userId.eq(userId))
                )
                .transform(
                        groupBy(review.reviewId).list(
                                new QReviewResponseRecord(
                                        review.reviewId,
                                        review.rating,
                                        review.content,
                                        review.createdAt,
                                        review.book.id,
                                        list(image.uuid),
                                        review.userId
                                )
                        )
                ).stream()
                .findFirst();
    }

    @Override
    public List<ReviewResponseRecord> getAllBy() {
        return queryFactory.from(review)
                .leftJoin(review.images, image)
                .transform(
                        groupBy(review.reviewId).list(
                                new QReviewResponseRecord(
                                        review.reviewId,
                                        review.rating,
                                        review.content,
                                        review.createdAt,
                                        review.book.id,
                                        list(image.uuid),
                                        review.userId
                                )
                        )
                );
    }

    @Override
    public List<ReviewResponseRecord> getAllByUserId(String userId) {
        return queryFactory.from(review)
                .leftJoin(review.images, image)
                .where(review.userId.eq(userId))
                .transform(
                        groupBy(review.reviewId).list(
                                new QReviewResponseRecord(
                                        review.reviewId,
                                        review.rating,
                                        review.content,
                                        review.createdAt,
                                        review.book.id,
                                        list(image.uuid),
                                        review.userId
                                )
                        )
                );
    }

    @Override
    public List<ReviewResponseRecord> getAllByBookId(long bookId) {
        return queryFactory.from(review)
                .leftJoin(review.images, image)
                .where(review.book.id.eq(bookId))
                .transform(
                        groupBy(review.reviewId).list(
                                new QReviewResponseRecord(
                                        review.reviewId,
                                        review.rating,
                                        review.content,
                                        review.createdAt,
                                        review.book.id,
                                        list(image.uuid),
                                        review.userId
                                )
                        )
                );
    }
}