package shop.dodream.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.projection.CategoryFlatProjection;
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
    @GetMapping("/categories/{category-id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable("category-id") Long categoryId) {
        CategoryResponse response = categoryService.getCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 카테고리 하위 카테고리 조회
    @GetMapping("/categories/{category-id}/children")
    public ResponseEntity<List<CategoryTreeResponse>> getCategoriesChildren(@PathVariable("category-id") Long categoryId) {
        List<CategoryTreeResponse> response = categoryService.getCategoriesChildren(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 카테고리 연관 카테고리 전체 조회
    @GetMapping("/categories/{category-id}/related")
    public ResponseEntity<List<CategoryTreeResponse>> getCategoriesRelated(@PathVariable("category-id") Long categoryId) {
        List<CategoryTreeResponse> response = categoryService.getCategoriesRelated(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 깊이 카테고리 전체 조회
    @GetMapping("/categories/{depth}/depth")
    public ResponseEntity<List<CategoryResponse>> getCategoriesDepth(@PathVariable Long depth) {
        List<CategoryResponse> response = categoryService.getCategoriesDepth(depth);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 카테고리 수정
    @PatchMapping("/admin/categories/{category-id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("category-id") Long categoryId,
                                                           @RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 카테고리 삭제
    @DeleteMapping("/admin/categories/{category-id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
