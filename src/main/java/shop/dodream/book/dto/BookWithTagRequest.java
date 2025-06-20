package shop.dodream.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookWithTagRequest {
    @NotNull(message = "태그 ID는 필수입니다.")
    private Long tagId;
}
