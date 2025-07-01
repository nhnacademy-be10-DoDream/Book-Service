package shop.dodream.book.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/public/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "도서 조회 API")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "도서 상세 조회(유저)", description = "도서 ID로 도서의 상세 정보를 조회합니다.")
    @GetMapping("{book-id}")
    public BookDetailResponse getBookById(@PathVariable("book-id") Long bookId){
        return bookService.getBookByIdForUser(bookId);
    }

    @Operation(summary = "도서 다건 조회", description = "도서 ID 리스트로 여러 도서를 한 번에 조회합니다.")
    @GetMapping
    public List<BookListResponseRecord> getBooksByIds(@RequestParam List<Long> ids){
        return bookService.findAllByIds(ids);
    }

    @Operation(summary = "도서 isbn 으로 도서 조회", description = "도서 isbn으로 도서 조회합니다.")
    @GetMapping("{isbn}")
    public BookResponse getBookByIsbn(@PathVariable("isbn") String isbn){
        return bookService.getBookByIsbn(isbn);
    }
}
