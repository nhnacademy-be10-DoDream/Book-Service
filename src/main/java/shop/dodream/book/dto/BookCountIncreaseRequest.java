package shop.dodream.book.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BookCountIncreaseRequest {

    @NotNull
    private Long bookId;

    @NotNull
    @Min(1)
    private Long bookCount;
}
