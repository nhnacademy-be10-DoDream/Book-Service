package shop.dodream.book.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class BookCountDecreaseResponse {
    private Long bookId;
    private Long remainBookCount;
    private boolean orderable;

}
