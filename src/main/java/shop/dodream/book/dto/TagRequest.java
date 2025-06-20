package shop.dodream.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TagRequest {
    @NotBlank(message = "태그 이름은 비어 있을 수 없습니다.")
    @Size(max = 20, message = "태그 이름은 20자를 초과할 수 없습니다.")
    private String tagName;
}
