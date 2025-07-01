package shop.dodream.book.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.annotation.ValidatedFiles;
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

    @PostMapping("/aladdin-api")
    public ResponseEntity<Void> aladdinRegisterBook(
            @RequestParam("isbn")
            @NotBlank(message = "isbn 은 필수값입니다.")
            @Pattern(regexp = "\\d{13}", message = "ISBN은 13자리 숫자여야합니다.")
            String isbn){
        bookService.registerBookByIsbn(isbn);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping()
    public ResponseEntity<Void> registerBook(@Valid @RequestPart("book") BookRegisterRequest bookRegisterRequest,
                                             @ValidatedFiles @RequestPart(value = "files", required = false)List<MultipartFile> files){
        bookService.registerBookDirect(bookRegisterRequest, files);
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

    @PutMapping("/{book-id}")
    public ResponseEntity<Void> updateBook(@PathVariable("book-id") Long bookId,
                                           @Validated @RequestBody BookUpdateRequest request,
                                           @ValidatedFiles @RequestPart(value = "files", required = false)List<MultipartFile> files){
        bookService.updateBook(bookId, request, files);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{book-id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("book-id") Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

}
