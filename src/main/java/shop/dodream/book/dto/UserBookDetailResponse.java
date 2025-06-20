package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;


@Getter
@AllArgsConstructor
public class UserBookDetailResponse {

    private String title;
    private String author;
    private String description;
    private String publisher;
    private String isbn;
    private LocalDate publishedAt;
    private Long salePrice;
    private Long regularPrice;
    private Boolean isGiftable;
    private String bookUrl;
    private Long discountRate;
    private Long likeCount;


}
