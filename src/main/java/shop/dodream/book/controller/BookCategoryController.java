package shop.dodream.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.*;
import shop.dodream.book.service.BookCategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;

    @PostMapping("/admin/books/{book-id}/categories")
    public ResponseEntity<BookWithCategoriesResponse> registerCategory(@PathVariable("book-id") Long bookId, @RequestBody @Valid BookWithCategoriesRequest request) {
        BookWithCategoriesResponse response = bookCategoryService.registerCategory(bookId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/books/{book-id}/categories")
    public ResponseEntity<List<CategoryTreeResponse>> getCategoriesByBookId(@PathVariable("book-id") Long bookId) {
        List<CategoryTreeResponse> response = bookCategoryService.getCategoriesByBookId(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/categories/{category-id}/books")
    public ResponseEntity<List<BookListResponse>> getBooksByCategoryId(@PathVariable("category-id") Long categoryId) {
        List<BookListResponse> response = bookCategoryService.getBooksByCategoryId(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/admin/books/{book-id}/categories/{category-id}")
    public ResponseEntity<BookWithCategoryResponse> getBooksByCategoryId(@PathVariable("book-id") Long bookId,
                                                                         @PathVariable("category-id") Long categoryId,
                                                                         @RequestBody @Valid BookWithCategoryRequest request) {
        BookWithCategoryResponse response = bookCategoryService.updateCategoryByBook(bookId, categoryId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/admin/books/{book-id}/categories")
    public ResponseEntity<Void> deleteCategoriesByBook(@PathVariable("book-id") Long bookId, @RequestBody @Valid BookWithCategoriesRequest request){
        bookCategoryService.deleteCategoriesByBook(bookId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
