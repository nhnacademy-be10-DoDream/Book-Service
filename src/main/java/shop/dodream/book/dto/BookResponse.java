package shop.dodream.book.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class BookResponse {

    private Long bookId;
    private String title;

    @QueryProjection
    public BookResponse(Long bookId, String title){
        this.bookId = bookId;
        this.title = title;
    }
}
