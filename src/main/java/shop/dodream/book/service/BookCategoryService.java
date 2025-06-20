package shop.dodream.book.service;

import shop.dodream.book.dto.*;

import java.util.List;

public interface BookCategoryService {
    BookWithCategoriesResponse registerCategory(Long BookId, BookWithCategoriesRequest request);
    List<CategoryTreeResponse> getCategoriesByBookId(Long bookId);
    List<BookListResponse> getBooksByCategoryId(Long categoryId);
    BookWithCategoryResponse updateCategoryByBook(Long BookId, Long categoryId, BookWithCategoryRequest request);
    void deleteCategoriesByBook(Long bookId, BookWithCategoriesRequest request);
}
