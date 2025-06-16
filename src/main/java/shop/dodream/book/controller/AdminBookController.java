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

    // 책 등록(naverApi) 활용
    @PostMapping
    public ResponseEntity<BookRegisterResponse> registerBook(@Validated @RequestBody BookRegisterRequest request){

        BookRegisterResponse response = bookService.registerBookByIsbn(request);
        return ResponseEntity.ok(response);
    }


    // 책 전체 리스트 조회
    @GetMapping
    public ResponseEntity<List<BookListResponse>> getAllBooks(){
        List<BookListResponse> bookListResponses = bookService.getAllBooks();
        return ResponseEntity.ok(bookListResponses);
    }



    // 책 단일 조회
    @GetMapping("{bookId}")
    public ResponseEntity<AdminBookDetailResponse> getBookById(@PathVariable Long bookId){
        AdminBookDetailResponse adminBookDetailResponse = bookService.getBookByIdForAdmin(bookId);
        return ResponseEntity.ok(adminBookDetailResponse);
    }

    // 책 수정
    @PatchMapping("{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable Long bookId,
                                           @Validated @RequestBody BookUpdateRequest request){
        bookService.updateBook(bookId, request);
        return ResponseEntity.noContent().build();
    }

    // 책 삭제
    @DeleteMapping("{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    // 재고 차감 API 외부 호출용
    @PostMapping("/decrease-bookCount")
    public ResponseEntity<BookCountDecreaseResponse> decreaseBookCount(@Validated @RequestBody BookCountDecreaseRequest request){
        BookCountDecreaseResponse response = bookService.decreaseBookCount(request);

        return ResponseEntity.ok(response);
    }





}
