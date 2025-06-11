package shop.dodream.book.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.dodream.book.dto.BookRegisterRequest;
import shop.dodream.book.dto.BookRegisterResponse;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("admin/books")
    public ResponseEntity<BookRegisterResponse> registerBook(@RequestBody BookRegisterRequest request){

        BookRegisterResponse response = bookService.registerBookByIsbn(request);
        return ResponseEntity.ok(response);
    }



}
