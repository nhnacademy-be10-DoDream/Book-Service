package shop.dodream.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.BookStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BookStatus status;
    private Long regularPrice;
    private Long salePrice;
    private Boolean isGiftable;
    private long viewCount;
    private long searchCount;
    private ZonedDateTime createdAt;
}
