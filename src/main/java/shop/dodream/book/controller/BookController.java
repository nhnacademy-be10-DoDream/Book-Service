package shop.dodream.book.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.dto.UserBookDetailResponse;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("{book-id}")
    public UserBookDetailResponse getBookById(@PathVariable("book-id") Long bookId){
        return bookService.getBookByIdForUser(bookId);
    }


    @GetMapping
    public List<BookListResponse> getBooksByIds(@RequestParam List<Long> ids){
        return bookService.findAllByIds(ids);
    }
}
