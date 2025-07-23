package shop.dodream.book.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import shop.dodream.book.dto.projection.QReviewResponseRecord;
import shop.dodream.book.dto.projection.QReviewSummaryResponse;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.dto.projection.ReviewSummaryResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static shop.dodream.book.entity.QImage.image;
import static shop.dodream.book.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewQuerydslRepositoryImpl implements ReviewQuerydslRepository {

    private final JPQLQueryFactory queryFactory;

    @Override
    public Optional<ReviewResponseRecord> getByReviewId(long reviewId) {
        List<ReviewResponseRecord> results = queryFactory
                .from(review)
                .leftJoin(review.images, image)
                .where(review.reviewId.eq(reviewId))
                .transform(groupBy(review.reviewId).list(createReviewProjection()));

        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public Optional<ReviewResponseRecord> getByReviewIdAndUserId(long reviewId, String userId) {
        List<ReviewResponseRecord> results = queryFactory
                .from(review)
                .leftJoin(review.images, image)
                .where(buildReviewAndUserCondition(reviewId, userId))
                .transform(groupBy(review.reviewId).list(createReviewProjection()));

        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }

    @Override
    public Page<ReviewResponseRecord> getAllBy(String userId, Pageable pageable) {
        BooleanBuilder builder = buildUserCondition(userId);
        return getReviewsPage(builder, pageable);
    }

    @Override
    public Page<ReviewResponseRecord> getAllByBookId(long bookId, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder(review.book.id.eq(bookId));
        return getReviewsPage(builder, pageable);
    }

    @Override
    public Optional<ReviewSummaryResponse> findReviewSummaryByBookId(long bookId) {

        ReviewSummaryResponse reviewSummaryResponse = queryFactory
                .select(new QReviewSummaryResponse(
                        review.rating.avg(),
                        review.count()
                ))
                .from(review)
                .where(review.book.id.eq(bookId))
                .fetchOne();
        return Optional.ofNullable(reviewSummaryResponse)
                .map(r -> {
                    if (r.getRatingAvg() == null) {
                        r.setRatingAvg(0.0);
                    }
                    return r;
                })
                .or(() -> Optional.of(new ReviewSummaryResponse(0.0, 0L)));
    }

    @Override
    public List<Long> getByNoWriteReview(List<Long> orderItemIds, String userId) {
        if (orderItemIds.isEmpty()) {
            return List.of();
        }
        List<Long> writtenIds = queryFactory.from(review)
                .select(review.orderItemId)
                .where(review.orderItemId.in(orderItemIds)
                        .and(review.userId.eq(userId)))
                .fetch();

        Set<Long> writtenIdSet = new HashSet<>(writtenIds);

        return orderItemIds.stream()
                .filter(id -> !writtenIdSet.contains(id))
                .toList();
    }

    private Page<ReviewResponseRecord> getReviewsPage(BooleanBuilder builder, Pageable pageable) {
        long totalCount = getTotalCount(builder);
        if (totalCount == 0) {
            return createEmptyPage(pageable);
        }

        List<Long> reviewIds = getReviewIdsWithPagination(builder, pageable);
        if (reviewIds.isEmpty()) {
            return createEmptyPage(pageable, totalCount);
        }

        List<ReviewResponseRecord> reviews = getReviewsWithImages(reviewIds);
        return new PageImpl<>(reviews, pageable, totalCount);
    }


    private QReviewResponseRecord createReviewProjection() {
        return new QReviewResponseRecord(
                review.reviewId,
                review.rating,
                review.content,
                review.createdAt,
                review.book.id,
                review.userId,
                list(image.uuid)
        );
    }

    private BooleanBuilder buildReviewAndUserCondition(long reviewId, String userId) {
        return new BooleanBuilder()
                .and(review.reviewId.eq(reviewId))
                .and(review.userId.eq(userId));
    }

    private BooleanBuilder buildUserCondition(String userId) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(userId)) {
            builder.and(review.userId.eq(userId));
        }
        return builder;
    }

    private long getTotalCount(BooleanBuilder builder) {
        return Optional.ofNullable(queryFactory
                .select(review.count())
                .from(review)
                .where(builder)
                .fetchOne()).orElse(0L);
    }

    private List<Long> getReviewIdsWithPagination(BooleanBuilder builder, Pageable pageable) {
        return queryFactory
                .select(review.reviewId)
                .from(review)
                .where(builder)
                .orderBy(review.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private List<ReviewResponseRecord> getReviewsWithImages(List<Long> reviewIds) {
        return queryFactory
                .from(review)
                .leftJoin(review.images, image)
                .where(review.reviewId.in(reviewIds))
                .orderBy(review.createdAt.desc())
                .transform(groupBy(review.reviewId).list(createReviewProjection()));
    }

    private PageImpl<ReviewResponseRecord> createEmptyPage(Pageable pageable) {
        return new PageImpl<>(List.of(), pageable, 0L);
    }

    private PageImpl<ReviewResponseRecord> createEmptyPage(Pageable pageable, long totalCount) {
        return new PageImpl<>(List.of(), pageable, totalCount);
    }
}