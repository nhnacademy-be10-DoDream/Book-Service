package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BookRegisterResponse {
    private Long bookId;
    private String title;
    private String description;
    private String author;
    private String publisher;
    private LocalDate publishedAt;
    private String isbn;
    private Long regularPrice;
    private BookStatus status;
    private Long salePrice;
    private Boolean isGiftable;
    private ZonedDateTime createdAt;
//    private Long searchCount;
//    private Long viewCount;
    private Long bookCount;
    private Long discountRate;
}
