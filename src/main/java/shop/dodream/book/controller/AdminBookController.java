package shop.dodream.book.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> registerBook(@Validated @RequestBody BookRegisterRequest request){
        bookService.registerBookByIsbn(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    public List<BookListResponseRecord> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{book-id}")
    public BookDetailResponse getBookById(@PathVariable("book-id") Long bookId){
        return bookService.getBookByIdForAdmin(bookId);
    }

    @PatchMapping("/{book-id}")
    public ResponseEntity<Void> updateBook(@PathVariable("book-id") Long bookId,
                                           @Validated @RequestBody BookUpdateRequest request){
        bookService.updateBook(bookId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{book-id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("book-id") Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

}
