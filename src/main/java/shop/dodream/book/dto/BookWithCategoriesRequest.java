package shop.dodream.book.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookWithCategoriesRequest {
    @NotNull(message = "카테고리 리스트는 null일 수 없습니다.")
    @Size(max = 10, message = "카테고리는 최대 10개까지 선택 가능합니다.")
    private List<Long> categoryIds;
}
