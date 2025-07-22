package shop.dodream.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CategoryRequest {
    @NotBlank(message = "카테고리 이름은 비어 있을 수 없습니다.")
    @Size(max = 50, message = "카테고리 이름은 50자를 초과할 수 없습니다.")
    private String categoryName;
    @Min(value = 1, message = "카테고리 depth는 최소 1 이상이어야 합니다.")
    private Long depth;
    @Positive(message = "부모 카테고리 ID는 양수여야 합니다.")
    private Long parentId;

    public CategoryRequest(String categoryName, long depth) {
        this.categoryName = categoryName;
        this.depth = depth;
    }
}
