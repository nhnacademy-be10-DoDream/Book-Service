package shop.dodream.book.dto.projection;

import com.querydsl.core.annotations.QueryProjection;

import java.time.ZonedDateTime;
import java.util.List;

public record ReviewResponseRecord(
        long reviewId,
        Short rating,
        String content,
        ZonedDateTime createdAt,
        long bookId,
        String userId,
        List<String> images
) {

    @QueryProjection
    public ReviewResponseRecord{}
}