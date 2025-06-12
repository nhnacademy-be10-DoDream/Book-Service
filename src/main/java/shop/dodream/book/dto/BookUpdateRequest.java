package shop.dodream.book.dto;


import lombok.Getter;
import lombok.Setter;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDate;

@Setter
@Getter
public class BookUpdateRequest {
    private String title;
    private String description;
    private String author;
    private String publisher;
    private LocalDate publishedAt;
    private Long regularPrice; // 정가
    private Long salePrice; // 할인가
    private Boolean isGiftable;
    private Long bookCount;
    private String bookUrl;

}
