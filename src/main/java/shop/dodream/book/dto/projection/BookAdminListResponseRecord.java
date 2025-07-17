package shop.dodream.book.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import shop.dodream.book.entity.BookStatus;

import java.time.ZonedDateTime;
import java.util.List;

public record BookAdminListResponseRecord(
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
    public BookAdminListResponseRecord(Long bookId, String title, String author, String isbn,
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
