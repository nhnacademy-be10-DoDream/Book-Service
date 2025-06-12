package shop.dodream.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BookRegisterRequest {

    @NotBlank(message = "isbn 은 필수 값 입니다.")
    private String isbn;

    private String description;

    private long discountRate;
}
