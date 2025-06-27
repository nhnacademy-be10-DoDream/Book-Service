package shop.dodream.book.dto.projection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDetailResponse {
    private Long bookId;
    private String title;
    private String author;
    private String description;
    private String publisher;
    private String isbn;
    private LocalDate publishedAt;
    private Long salePrice;
    private Long regularPrice;
    private Boolean isGiftable;
    private List<String> bookUrls;
    private Long discountRate;
    private Long likeCount;

    private BookStatus status;
    private ZonedDateTime createdAt;
    private Long searchCount;
    private Long viewCount;
    private Long bookCount;

    @QueryProjection
    public BookDetailResponse(Long bookId, String title, String author, String description, String publisher,
                              String isbn, LocalDate publishedAt, Long salePrice, Long regularPrice,
                              Boolean isGiftable, List<String> bookUrls, Long discountRate, Long likeCount) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publishedAt = publishedAt;
        this.salePrice = salePrice;
        this.regularPrice = regularPrice;
        this.isGiftable = isGiftable;
        this.bookUrls = bookUrls;
        this.discountRate = discountRate;
        this.likeCount = likeCount;
    }

    @QueryProjection
    public BookDetailResponse(Long bookId, String title, String author, String description, String publisher,
                              String isbn, LocalDate publishedAt, Long salePrice, Long regularPrice,
                              Boolean isGiftable, List<String> bookUrls, Long discountRate, Long likeCount,
                              BookStatus status, ZonedDateTime createdAt, Long searchCount, Long viewCount, Long bookCount) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publishedAt = publishedAt;
        this.salePrice = salePrice;
        this.regularPrice = regularPrice;
        this.isGiftable = isGiftable;
        this.bookUrls = bookUrls;
        this.discountRate = discountRate;
        this.likeCount = likeCount;
        this.status = status;
        this.createdAt = createdAt;
        this.searchCount = searchCount;
        this.viewCount = viewCount;
        this.bookCount = bookCount;
    }
}
