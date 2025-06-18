package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookListResponse {
    private Long bookId;
    private String title;
    private String author;
    private String isbn;
    private Long regularPrice;
    private Long salePrice;
    private String bookUrl;
}
