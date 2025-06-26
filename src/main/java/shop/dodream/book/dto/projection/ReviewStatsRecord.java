package shop.dodream.book.dto.projection;

import com.querydsl.core.annotations.QueryProjection;

public record ReviewStatsRecord(Long reviewCount, Float ratingAvg) {

    @QueryProjection
    public ReviewStatsRecord{}
}
