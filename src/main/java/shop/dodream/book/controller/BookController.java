package shop.dodream.book.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/public/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("{book-id}")
    public BookDetailResponse getBookById(@PathVariable("book-id") Long bookId){
        return bookService.getBookByIdForUser(bookId);
    }


    @GetMapping
    public List<BookListResponseRecord> getBooksByIds(@RequestParam List<Long> ids){
        return bookService.findAllByIds(ids);
    }
}
