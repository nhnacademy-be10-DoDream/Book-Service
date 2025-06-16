package shop.dodream.book.dto.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryFlatProjection {
    private Long categoryId;
    private String categoryName;
    private Long depth;
    private Long parentId;

    @QueryProjection
    public CategoryFlatProjection(Long categoryId, String categoryName, Long depth, Long parentId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.depth = depth;
        this.parentId = parentId;
    }
}
