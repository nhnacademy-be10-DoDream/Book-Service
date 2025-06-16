package shop.dodream.book.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.UserBookDetailResponse;
import shop.dodream.book.service.BookService;



@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;


    @GetMapping("{bookId}")
    public ResponseEntity<UserBookDetailResponse> getBookById(@PathVariable Long bookId){
        UserBookDetailResponse userBookDetailResponse = bookService.getBookByIdForUser(bookId);
        return ResponseEntity.ok(userBookDetailResponse);
    }



}
