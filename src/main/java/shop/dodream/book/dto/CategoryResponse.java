package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CategoryResponse {
    private Long categoryId;
    private String categoryName;
    private Long depth;
    private Long parentId;

    public CategoryResponse(Category category) {
        this.categoryId = category.getId();
        this.categoryName = category.getCategoryName();
        this.depth = category.getDepth();
        this.parentId = category.getParent() != null
                ? category.getParent().getId()
                : null;
    }
}
