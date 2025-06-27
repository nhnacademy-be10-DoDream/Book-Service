package shop.dodream.book.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.dto.projection.CategoryWithParentProjection;
import shop.dodream.book.dto.projection.QCategoryFlatProjection;
import shop.dodream.book.entity.QCategory;

import java.util.List;

@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory; // 실제 쿼리를 만들어 실행하는 역할
    private static final QCategory category = QCategory.category; // 테이블, 컬럼, 관계를 코드로 표현하는 역할

    @Override
    public List<CategoryFlatProjection> findAllFlat() {
        return queryFactory
                .select(new QCategoryFlatProjection(
                        category.id,
                        category.categoryName,
                        category.depth,
                        category.parent.id
                ))
                .from(category)
                .fetch(); // -> DB, <- 결과를 리스트 형태
    }

    @Override
    public List<CategoryWithParentProjection> findAllByIdsWithParent(List<Long> categoryIds) {
        QCategory parent = new QCategory("parent");

        return queryFactory
                .select(Projections.constructor(CategoryWithParentProjection.class,
                        category.id,
                        category.categoryName,
                        category.depth,
                        category.parent.id,
                        category.parent.categoryName
                ))
                .from(category)
                .leftJoin(category.parent, parent)
                .where(category.id.in(categoryIds))
                .fetch();
    }
}
