package shop.dodream.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookWithCategoriesResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.IdsListRequest;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookCategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Book Category", description = "도서 카테고리 관련 API")
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;

    @Operation(summary = "도서에 카테고리 등록", description = "도서에 하나 이상의 카테고리를 등록합니다.")
    @PostMapping("/admin/books/{book-id}/categories")
    public ResponseEntity<BookWithCategoriesResponse> registerCategory(@PathVariable("book-id") Long bookId,
                                                                       @RequestBody @Valid IdsListRequest categoryIds) {
        BookWithCategoriesResponse response = bookCategoryService.registerCategory(bookId, categoryIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "도서의 카테고리 트리 조회", description = "도서에 등록된 카테고리 트리 구조를 조회합니다.")
    @GetMapping("/public/books/{book-id}/categories")
    public List<CategoryTreeResponse> getCategoriesByBookId(@PathVariable("book-id") Long bookId) {
        return bookCategoryService.getCategoriesByBookId(bookId);
    }

    @Operation(summary = "카테고리에 속한 도서 조회(페이징)", description = "카테고리에 속한 도서 목록을 페이징 처리하여 조회합니다.")
    @GetMapping("/public/categories/{category-id}/books")
    public Page<BookListResponseRecord> getBooksByCategoryId(@PathVariable("category-id") Long categoryId,
                                                             @PageableDefault(size = 10) Pageable pageable) {
        return bookCategoryService.getBooksByCategoryId(categoryId, pageable);
    }

    @Operation(summary = "도서의 카테고리 수정",
            description = "도서에 등록된 카테고리를 다른 카테고리로 수정합니다.")
    @PutMapping("/admin/books/{book-id}/categories/{category-id}")
    public Long updateCategoryByBook(@PathVariable("book-id") Long bookId,
                                     @PathVariable("category-id") Long categoryId,
                                     @RequestParam(value = "new-category-id") Long newCategoryId) {
        return bookCategoryService.updateCategoryByBook(bookId, categoryId, newCategoryId);
    }

    @Operation(summary = "도서의 카테고리 삭제", description = "도서에 등록된 하나 이상의 카테고리를 삭제합니다.")
    @DeleteMapping("/admin/books/{book-id}/categories")
    public ResponseEntity<Void> deleteCategoriesByBook(@PathVariable("book-id") Long bookId,
                                                       @RequestBody IdsListRequest categoryIds){
        bookCategoryService.deleteCategoriesByBook(bookId, categoryIds);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
