package shop.dodream.book.repository.querydsl;

import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.dto.projection.CategoryWithParentProjection;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryFlatProjection> findAllFlat();
    List<CategoryWithParentProjection> findAllByIdsWithParent(List<Long> ids);
}
