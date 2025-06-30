package shop.dodream.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookSortType;
import shop.dodream.book.service.BookSearchService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookSearchController {
    private final BookSearchService bookSearchService;

    @GetMapping("/search")
    public List<BookItemResponse> searchBooks(@RequestParam String keyword,
                                                              @RequestParam(value = "sort", required = false, defaultValue = "NONE") BookSortType sort) {
        return bookSearchService.searchBooks(keyword, sort);
    }
}
