package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import shop.dodream.book.entity.Book;

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



    public BookListResponse(Book book){
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.regularPrice = book.getRegularPrice();
        this.salePrice = book.getSalePrice();
        this.bookUrl = book.getBookUrl();
    }

}
