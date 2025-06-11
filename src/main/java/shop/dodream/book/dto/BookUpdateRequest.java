package shop.dodream.book.dto;


import lombok.Getter;
import lombok.Setter;
import shop.dodream.book.entity.BookStatus;

@Setter
@Getter
public class BookUpdateRequest {
    private String description;
    private BookStatus status; // removed 만허용 나머진 자동처리

}
