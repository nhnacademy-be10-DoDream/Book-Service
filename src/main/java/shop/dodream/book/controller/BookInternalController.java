package shop.dodream.book.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookCountDecreaseRequest;
import shop.dodream.book.dto.BookCountDecreaseResponse;
import shop.dodream.book.dto.BookCountIncreaseRequest;
import shop.dodream.book.service.BookService;


@RestController
@RequestMapping("/public/books/internal")
@RequiredArgsConstructor
@Tag(name = "Book Internal", description = "도서 재고 관리 API")
public class BookInternalController {
    private final BookService bookService;

    @Operation(summary = "도서 재고 감소", description = "주문 시 도서 재고를 차감하는 API입니다.")
    @PostMapping("/decrease-bookCount")
    public BookCountDecreaseResponse decreaseBookCount(@Validated @RequestBody BookCountDecreaseRequest request){

        return bookService.decreaseBookCount(request);
    }

    @Operation(summary = "도서 재고 추가", description = "환불 시 도서 재고를 증가하는 API입니다.")
    @PostMapping("/increase-bookCount")
    public ResponseEntity<Void> increaseBookCount(@Validated @RequestBody BookCountIncreaseRequest request){
        bookService.increaseBookCount(request);

        return ResponseEntity.noContent().build();
    }

}