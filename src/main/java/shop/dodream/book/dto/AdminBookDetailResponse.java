package shop.dodream.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminBookDetailResponse {

    private Long id;
    private String title;
    private String author;
    private String description;
    private String publisher;
    private String isbn;
    private LocalDate publishedAt;
    private BookStatus status;
    private Long salePrice;
    private Long regularPrice;
    private Boolean isGiftable;
    private Long viewCount;
    private Long searchCount;
    private ZonedDateTime createdAt;
    private Long bookCount;
    private String bookUrl;
    private Long discountRate;



}
