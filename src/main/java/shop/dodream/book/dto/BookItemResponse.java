package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

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
    private Date publishedAt;
    private Long viewCount;
    private Float ratingAvg;
    private Long reviewCount;


    public BookItemResponse(BookDocument document) {
        this.bookId = document.getBookId();
        this.title = document.getTitle();
        this.description = document.getDescription();
        this.author = document.getAuthor();
        this.publisher = document.getPublisher();
        this.salePrice = document.getSalePrice();
        this.publishedAt = document.getPublishedAt();
        this.viewCount = document.getViewCount();
        this.ratingAvg = document.getRatingAvg();
        this.reviewCount = document.getReviewCount();
    }
}
