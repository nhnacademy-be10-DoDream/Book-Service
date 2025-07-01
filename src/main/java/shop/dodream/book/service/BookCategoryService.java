package shop.dodream.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.BookWithCategoriesResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.IdsListRequest;
import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;

public interface BookCategoryService {
    BookWithCategoriesResponse registerCategory(Long bookId, IdsListRequest categoryIds);
    List<CategoryTreeResponse> getCategoriesByBookId(Long bookId);
    Page<BookListResponseRecord> getBooksByCategoryId(Long categoryId, Pageable pageable);
    Long updateCategoryByBook(Long bookId, Long categoryId, Long newCategoryId);
    void deleteCategoriesByBook(Long bookId, IdsListRequest categoryIds);
}
