package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class    CategoryResponse {
    private Long categoryId;
    @Setter
    private String categoryName;
    @Setter
    private Long depth;
    @Setter
    private Long parentId;

    public CategoryResponse(Category category) {
        this.categoryId = category.getId();
        this.categoryName = category.getCategoryName();
        this.depth = category.getDepth();
        this.parentId = category.getParent() != null
                ? category.getParent().getId()
                : null;
    }
    public CategoryResponse(Long categoryId, String categoryName, Long depth, Long parentId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.depth = depth;
        this.parentId = parentId;
    }
}
