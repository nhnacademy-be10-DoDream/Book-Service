package shop.dodream.book.dto.projection;

public record CategoryWithParentProjection(
        Long id,
        String categoryName,
        Long depth,
        Long parentId,
        String parentName
) {}
