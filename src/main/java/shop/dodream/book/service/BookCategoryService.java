package shop.dodream.book.service;

import shop.dodream.book.dto.*;

import java.util.List;

public interface BookCategoryService {
    BookWithCategoriesResponse registerCategory(Long BookId, List<Long> categoryIds);
    List<CategoryTreeResponse> getCategoriesByBookId(Long bookId);
    List<BookListResponse> getBooksByCategoryId(Long categoryId);
    Long updateCategoryByBook(Long BookId, Long categoryId, Long newCategoryId);
    void deleteCategoriesByBook(Long bookId, List<Long> categoryIds);
}
