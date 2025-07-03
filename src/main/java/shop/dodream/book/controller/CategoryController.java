package shop.dodream.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category", description = "카테고리 관리 API")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 등록
    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 카테고리 조회
    @Operation(summary = "전체 카테고리 조회", description = "등록된 모든 카테고리를 조회합니다.")
    @GetMapping("/public/categories")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getCategories();
    }

    // 특정 카테고리 조회
    @Operation(summary = "카테고리 상세 조회", description = "카테고리 ID로 상세 정보를 조회합니다.")
    @GetMapping("/public/categories/{category-id}")
    public CategoryResponse getCategory(@PathVariable("category-id") Long categoryId) {
        return categoryService.getCategory(categoryId);
    }

    // 특정 카테고리 하위 카테고리 조회
    @Operation(summary = "카테고리 하위 조회", description = "카테고리 ID로 하위 카테고리 목록을 조회합니다.")
    @GetMapping("/public/categories/{category-id}/children")
    public List<CategoryTreeResponse> getCategoriesChildren(@PathVariable("category-id") Long categoryId) {
        return categoryService.getCategoriesChildren(categoryId);
    }

    // 특정 카테고리 연관 카테고리 전체 조회
    @Operation(summary = "카테고리 연관 트리 조회", description = "카테고리 ID로 연관된 전체 카테고리 트리를 조회합니다.")
    @GetMapping("/public/categories/{category-id}/related")
    public List<CategoryTreeResponse> getCategoriesRelated(@PathVariable("category-id") Long categoryId) {
        return categoryService.getCategoriesRelated(categoryId);
    }

    // 특정 카테고리 깊이별 조회
    @Operation(summary = "카테고리 깊이별 조회", description = "카테고리 depth로 카테고리 목록을 조회합니다.")
    @GetMapping("/public/categories/{depth}/depth")
    public List<CategoryResponse> getCategoriesDepth(@PathVariable Long depth) {
        return categoryService.getCategoriesDepth(depth);
    }

    // 카테고리 수정
    @Operation(summary = "카테고리 수정", description = "카테고리 ID로 카테고리 정보를 수정합니다.")
    @PutMapping("/admin/categories/{category-id}")
    public CategoryResponse updateCategory(@PathVariable("category-id") Long categoryId,
                                                           @RequestBody @Valid CategoryRequest request) {
        return categoryService.updateCategory(categoryId, request);
    }

    // 카테고리 삭제
    @Operation(summary = "카테고리 삭제", description = "카테고리 ID로 카테고리를 삭제합니다.")
    @DeleteMapping("/admin/categories/{category-id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
