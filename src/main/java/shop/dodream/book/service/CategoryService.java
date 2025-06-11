package shop.dodream.book.service;

import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getCategories();
    List<CategoryResponse> getCategoriesByBook(Long bookId);
    List<BookResponse> getBooksByCategory(Long categoryId);
    CategoryResponse updateCategory(Long categoryId, CategoryRequest request);
    void deleteCategory(Long categoryId);
}
