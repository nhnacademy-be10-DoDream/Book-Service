package shop.dodream.book.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookLikeCountResponse;
import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.dto.UserBookDetailResponse;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;



    @GetMapping("{book-id}")
    public ResponseEntity<UserBookDetailResponse> getBookById(@PathVariable("book-id") Long bookId){
        UserBookDetailResponse userBookDetailResponse = bookService.getBookByIdForUser(bookId);
        return ResponseEntity.ok(userBookDetailResponse);
    }


    // 도서 좋아요 수 조회
    @GetMapping("/{book-id}/likes/count")
    ResponseEntity<BookLikeCountResponse> getBookLikeCount(@PathVariable("book-id") Long bookId){
        BookLikeCountResponse bookLikeCountResponse = bookService.getBookLikeCount(bookId);
        return ResponseEntity.ok(bookLikeCountResponse);
    }

    @GetMapping
    public ResponseEntity<List<BookListResponse>> getBooksByIds(@RequestParam List<Long> ids){

        List<BookListResponse> bookListResponse = bookService.findAllByIds(ids);
        return ResponseEntity.ok(bookListResponse);
    }



}
