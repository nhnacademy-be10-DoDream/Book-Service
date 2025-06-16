package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class AdminBookDetailResponse {


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
    private Long searchCount;
    private Long viewCount;
    private Long bookCount;
    private String bookUrl;
    private Long discountRate;



    public AdminBookDetailResponse(Book book){
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.publishedAt = book.getPublishedAt();
        this.isbn = book.getIsbn();
        this.regularPrice = book.getRegularPrice();
        this.status = book.getStatus();
        this.salePrice = book.getSalePrice();
        this.isGiftable = book.getIsGiftable();
        this.createdAt = book.getCreatedAt();
        this.searchCount = book.getSearchCount();
        this.viewCount = book.getViewCount();
        this.bookCount = book.getBookCount();
        this.bookUrl = book.getBookUrl();
        this.discountRate = book.getDiscountRate();
    }
}
