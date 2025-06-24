package shop.dodream.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookSearchResponse;
import shop.dodream.book.service.BookSearchService;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String keyword) {
        Map<String, Object> results = bookSearchService.searchBooks(keyword);
        return ResponseEntity.ok(results);
    }
}
