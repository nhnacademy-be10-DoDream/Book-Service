package shop.dodream.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.entity.Category;
import shop.dodream.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    // 카테고리 등록
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getCategories();
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
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long categoryId,
                                                           @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.updateCategory(categoryId, categoryRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 카테고리 삭제
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
