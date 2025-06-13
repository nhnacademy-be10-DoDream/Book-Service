package shop.dodream.book.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.UserBookDetailProjection;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/books")
public class BookController {

    // 사용자 화면 책 조회
    @Autowired
    private BookService bookService;


    @GetMapping("{bookId}")
    public ResponseEntity<UserBookDetailProjection> getBookById(@PathVariable Long bookId){
        UserBookDetailProjection userBookDetailProjection = bookService.getBookByIdForUser(bookId);
        return ResponseEntity.ok(userBookDetailProjection);
    }



}
