package shop.dodream.book.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.*;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookRegisterResponse> registerBook(@RequestBody BookRegisterRequest request){

        BookRegisterResponse response = bookService.registerBookByIsbn(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<BookListResponse>> getAllBooks(){
        List<BookListResponse> bookListResponse = bookService.getAllBooks();
        return ResponseEntity.ok(bookListResponse);
    }



    @GetMapping("{bookId}")
    public ResponseEntity<BookDetailResponse> getBookById(@PathVariable Long bookId){
        BookDetailResponse bookDetailResponse = bookService.getBookById(bookId);
        return ResponseEntity.ok(bookDetailResponse);
    }

    @PatchMapping("{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable Long bookId,
                                           @RequestBody BookUpdateRequest request){
        bookService.updateBook(bookId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }



}
