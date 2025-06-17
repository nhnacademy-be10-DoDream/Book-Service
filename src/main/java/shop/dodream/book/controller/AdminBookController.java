package shop.dodream.book.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.*;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookRegisterResponse> registerBook(@Validated @RequestBody BookRegisterRequest request){

        BookRegisterResponse response = bookService.registerBookByIsbn(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<BookListResponse>> getAllBooks(){
        List<BookListResponse> bookListResponses = bookService.getAllBooks();
        return ResponseEntity.ok(bookListResponses);
    }



    @GetMapping("{bookId}")
    public ResponseEntity<AdminBookDetailResponse> getBookById(@PathVariable Long bookId){
        AdminBookDetailResponse adminBookDetailResponse = bookService.getBookByIdForAdmin(bookId);
        return ResponseEntity.ok(adminBookDetailResponse);
    }

    @PatchMapping("{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable Long bookId,
                                           @Validated @RequestBody BookUpdateRequest request){
        bookService.updateBook(bookId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/decrease-bookCount")
    public ResponseEntity<BookCountDecreaseResponse> decreaseBookCount(@Validated @RequestBody BookCountDecreaseRequest request){
        BookCountDecreaseResponse response = bookService.decreaseBookCount(request);

        return ResponseEntity.ok(response);
    }





}
