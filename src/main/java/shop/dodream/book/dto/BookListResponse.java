package shop.dodream.book.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import shop.dodream.book.entity.Book;

@Getter
public class BookListResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Long regularPrice;
    private Long salePrice;
    private String bookUrl;


    @QueryProjection
    public BookListResponse(Long id, String title, String author, String isbn,
                            Long regularPrice, Long salePrice, String bookUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.bookUrl = bookUrl;
    }
}
