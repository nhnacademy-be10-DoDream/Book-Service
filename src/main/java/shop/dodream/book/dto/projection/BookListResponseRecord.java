package shop.dodream.book.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import shop.dodream.book.entity.BookStatus;

import java.time.ZonedDateTime;

public record BookListResponseRecord(
        Long bookId,
        String title,
        String author,
        String isbn,
        Long regularPrice,
        Long salePrice,
        String bookUrl,
        ZonedDateTime createdAt,
        BookStatus status
) {
    @QueryProjection
    public BookListResponseRecord(Long bookId, String title, String author, String isbn,
                                  Long regularPrice, Long salePrice, String bookUrl, ZonedDateTime createdAt, BookStatus status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.bookUrl = bookUrl;
        this.createdAt = createdAt;
        this.status = status;
    }
}