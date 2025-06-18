package shop.dodream.book.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookCountDecreaseRequest;
import shop.dodream.book.dto.BookCountDecreaseResponse;

import shop.dodream.book.service.BookService;


@RestController
@RequestMapping("/books/internal")
@RequiredArgsConstructor
public class BookInternalController {

    private final BookService bookService;


    @PostMapping("/decrease-bookCount")
    public ResponseEntity<BookCountDecreaseResponse> decreaseBookCount(@Validated @RequestBody BookCountDecreaseRequest request){
        BookCountDecreaseResponse response = bookService.decreaseBookCount(request);

        return ResponseEntity.ok(response);
    }

}
