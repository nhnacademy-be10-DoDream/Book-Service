package shop.dodream.book.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/books/{book-id}/categories")
    public ResponseEntity<BookWithCategoriesResponse> registerCategory(@PathVariable("book-id") Long bookId,
                                                                       @RequestBody IdsListRequest categoryIds) {
        BookWithCategoriesResponse response = bookCategoryService.registerCategory(bookId, categoryIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/books/{book-id}/categories")
    public List<CategoryTreeResponse> getCategoriesByBookId(@PathVariable("book-id") Long bookId) {
        return bookCategoryService.getCategoriesByBookId(bookId);
    }

    @GetMapping("/categories/{category-id}/books")
    public List<BookListResponse> getBooksByCategoryId(@PathVariable("category-id") Long categoryId) {
        return bookCategoryService.getBooksByCategoryId(categoryId);
    }

    @PatchMapping("/books/{book-id}/categories/{category-id}")
    public Long getBooksByCategoryId(@PathVariable("book-id") Long bookId,
                                     @PathVariable("category-id") Long categoryId,
                                     @RequestParam(value = "new-category-id") Long newCategoryId) {
        return bookCategoryService.updateCategoryByBook(bookId, categoryId, newCategoryId);
    }

    @DeleteMapping("/books/{book-id}/categories")
    public ResponseEntity<Void> deleteCategoriesByBook(@PathVariable("book-id") Long bookId,
                                                       @RequestBody IdsListRequest categoryIds){
        bookCategoryService.deleteCategoriesByBook(bookId, categoryIds);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
