package shop.dodream.book.service;

import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.projection.CategoryFlatProjection;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getCategories();
    CategoryResponse getCategory(Long categoryId);
    List<CategoryTreeResponse> getCategoriesChildren(Long categoryId);
    List<CategoryTreeResponse> getCategoriesRelated(Long categoryId);
    List<CategoryFlatProjection> getCategoriesPath(Long categoryId);
    List<CategoryResponse> getCategoriesDepth(Long depth);
    CategoryResponse updateCategory(Long categoryId, CategoryRequest request);
    void deleteCategory(Long categoryId);
}
