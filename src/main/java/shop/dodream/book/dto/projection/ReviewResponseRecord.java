package shop.dodream.book.dto.projection;

import com.querydsl.core.annotations.QueryProjection;

import java.time.ZonedDateTime;
import java.util.List;

public record ReviewResponseRecord(
        long reviewId,
        float rating,
        String content,
        ZonedDateTime createdAt,
        long bookId,
        List<String> images,
        String userId
) {
    @QueryProjection
    public ReviewResponseRecord {}
}