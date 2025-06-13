package shop.dodream.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 등록
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getCategories();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 카테고리 조회
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        CategoryResponse response = categoryService.getCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 카테고리 하위 카테고리 조회
    @GetMapping("/categories/{categoryId}/children")
    public ResponseEntity<List<CategoryTreeResponse>> getCategoriesChildren(@PathVariable Long categoryId) {
        List<CategoryTreeResponse> response = categoryService.getCategoriesChildren(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 카테고리 연관 카테고리 전체 조회
    @GetMapping("/categories/{categoryId}/related")
    public ResponseEntity<List<CategoryTreeResponse>> getCategoriesRelated(@PathVariable Long categoryId) {
        List<CategoryTreeResponse> response = categoryService.getCategoriesRelated(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 해당 도서의 카테고리 조회
    @GetMapping("/books/{bookId}/categories")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByBookId(@PathVariable Long bookId) {
        List<CategoryResponse> response = categoryService.getCategoriesByBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 해당 카테고리 도서 조회
    @GetMapping("/categories/{categoryId}/books")
    public ResponseEntity<List<BookResponse>> getBooksByCategoryId(@PathVariable Long categoryId) {
        List<BookResponse> response = categoryService.getBooksByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 카테고리 수정
    @PatchMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long categoryId,
                                                           @RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 카테고리 삭제
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
