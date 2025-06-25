package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemResponse {
    private Long bookId;
    private String title;
    private String description;
    private String author;
    private String publisher;
    private Long salePrice;
    private LocalDate publishedAt;


    public BookItemResponse(BookDocument document) {
        this.bookId = document.getBookId();
        this.title = document.getTitle();
        this.description = document.getDescription();
        this.author = document.getAuthor();
        this.publisher = document.getPublisher();
        this.salePrice = document.getSalePrice();
        this.publishedAt = document.getPublishedAt();
    }
}
