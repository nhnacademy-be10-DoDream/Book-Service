package shop.dodream.book.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;


@Getter
public class ReviewSummaryResponse {

    @Setter
    private Double ratingAvg;
    private Long reviewCount;


    @QueryProjection
    public ReviewSummaryResponse(Double ratingAvg, Long reviewCount){
        this.ratingAvg = ratingAvg;
        this.reviewCount = reviewCount;

    }
}
