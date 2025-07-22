package shop.dodream.book.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/public/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "도서 조회 API")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "도서 상세 조회(유저)", description = "도서 ID로 도서의 상세 정보를 조회합니다.")
    @GetMapping("{book-id}")
    public BookDetailResponse getBookById(@PathVariable("book-id") Long bookId){

        bookService.redisIncreaseViewCount(bookId);

        return bookService.getBookByIdForUser(bookId);
    }


    // TODO 해결
    @Operation(summary = "도서 조건 조회", description = "도서 ID 리스트로 해당 도서 목록을 조회합니다.")
    @GetMapping
    public List<BookListResponseRecord> getBooksByIds(@RequestParam List<Long> ids){
        return bookService.findAllByIds(ids);
    }

    @Operation(summary = "도서 전체 조회", description = "등록된 모든 도서를 조회합니다.")
    @GetMapping("/all")
    public List<BookListResponseRecord> getAllBooks(){
        return bookService.getAllBooks();
    }

}
