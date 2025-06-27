package shop.dodream.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 등록
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 카테고리 조회
    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getCategories();
    }

    // 특정 카테고리 조회
    @GetMapping("/{category-id}")
    public CategoryResponse getCategory(@PathVariable("category-id") Long categoryId) {
        return categoryService.getCategory(categoryId);
    }

    // 특정 카테고리 하위 카테고리 조회
    @GetMapping("/{category-id}/children")
    public List<CategoryTreeResponse> getCategoriesChildren(@PathVariable("category-id") Long categoryId) {
        return categoryService.getCategoriesChildren(categoryId);
    }

    // 특정 카테고리 연관 카테고리 전체 조회
    @GetMapping("/{category-id}/related")
    public List<CategoryTreeResponse> getCategoriesRelated(@PathVariable("category-id") Long categoryId) {
        return categoryService.getCategoriesRelated(categoryId);
    }

    // 특정 카테고리 연관 카테고리 전체 조회
    @GetMapping("/{depth}/depth")
    public List<CategoryResponse> getCategoriesDepth(@PathVariable Long depth) {
        return categoryService.getCategoriesDepth(depth);
    }

    // 카테고리 수정
    @PatchMapping("/{category-id}")
    public CategoryResponse updateCategory(@PathVariable("category-id") Long categoryId,
                                                           @RequestBody @Valid CategoryRequest request) {
        return categoryService.updateCategory(categoryId, request);
    }

    // 카테고리 삭제
    @DeleteMapping("/{category-id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
