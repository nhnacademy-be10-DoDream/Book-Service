package shop.dodream.book.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookCountDecreaseRequest {
    @NotNull(message = "도서 ID는 필수입니다.")
    private Long bookId;

    @Min(value = 1, message = "수량은 1이상이어야합니다.")
    @Max(value = 50, message = "수량은 최대 50개 까지 가능합니다.")
    private Long bookCount;
}
