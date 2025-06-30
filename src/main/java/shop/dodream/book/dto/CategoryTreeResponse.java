package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.Category;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CategoryTreeResponse extends CategoryResponse {
    private List<CategoryTreeResponse> children = new ArrayList<>();

    public CategoryTreeResponse(Category category) {
        super(category);
        this.children = category.getChildren().stream()
                .map(CategoryTreeResponse::new)
                .toList();
    }
    public CategoryTreeResponse(Long categoryId, String categoryName, Long depth, Long parentId) {
        super(categoryId, categoryName, depth, parentId);
    }
}

