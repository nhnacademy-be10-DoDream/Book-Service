package shop.dodream.book.repository.querydsl;

import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryFlatProjection> findAllFlat();
}
