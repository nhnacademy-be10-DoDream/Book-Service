package shop.dodream.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookSearchResponse;
import shop.dodream.book.service.BookSearchService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookSearchController {

    private final BookSearchService bookSearchService;

//    @PostMapping("/reindex")
//    public ResponseEntity<Void> reindexAllBooks() {
//        bookSearchService.indexAllBooks();
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/search")
    public ResponseEntity<BookSearchResponse> searchBooks(@RequestParam(required = false) String keyword) {
        BookSearchResponse response = bookSearchService.searchBooks(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
