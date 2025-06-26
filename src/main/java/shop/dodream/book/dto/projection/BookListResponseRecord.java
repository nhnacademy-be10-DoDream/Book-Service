package shop.dodream.book.dto.projection;

import com.querydsl.core.annotations.QueryProjection;

public record BookListResponseRecord(
        Long bookId,
        String title,
        String author,
        String isbn,
        Long regularPrice,
        Long salePrice,
        String bookUrl

) {
    @QueryProjection
    public BookListResponseRecord {}
}