package shop.dodream.book.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.*;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {
    private final BookService bookService;

    @PostMapping
    public BookRegisterResponse registerBook(@Validated @RequestBody BookRegisterRequest request){
        return bookService.registerBookByIsbn(request);
    }


    @GetMapping
    public ResponseEntity<List<BookListResponse>> getAllBooks(){
        List<BookListResponse> bookListResponses = bookService.getAllBooks();
        return ResponseEntity.ok(bookListResponses);
    }



    @GetMapping("{book-id}")
    public ResponseEntity<AdminBookDetailResponse> getBookById(@PathVariable("book-id") Long bookId){
        AdminBookDetailResponse adminBookDetailResponse = bookService.getBookByIdForAdmin(bookId);
        return ResponseEntity.ok(adminBookDetailResponse);
    }

    @PatchMapping("{book-id}")
    public ResponseEntity<Void> updateBook(@PathVariable("book-id") Long bookId,
                                           @Validated @RequestBody BookUpdateRequest request){
        bookService.updateBook(bookId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{book-id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("book-id") Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

}
