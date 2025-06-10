package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BookRequest {
    private String title;
    private String description;
    private String author;
    private String publisher;
    private LocalDateTime publishedAt;
    private String isbn;
    private Long regularPrice;
    private BookStatus status;
    private Long salePrice;
    private Boolean isGiftable;
    private LocalDateTime createdAt;
}
