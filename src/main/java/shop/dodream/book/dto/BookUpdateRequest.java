package shop.dodream.book.dto;


import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BookUpdateRequest {

    private String title;
    private String description;
    private String author;
    private String publisher;
    private LocalDate publishedAt;

    @Min(value = 0, message = "정가는 0이상이어야합니다.")
    private Long regularPrice; // 정가

    @Min(value = 0, message = "할인가는 0이상이어야합니다.")
    private Long salePrice; // 할인가

    private Boolean isGiftable;

    @Min(value = 0, message = "수량은 0이상이어야합니다.")
    private Long bookCount;

}
