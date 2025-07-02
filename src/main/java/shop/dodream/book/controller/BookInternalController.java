package shop.dodream.book.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dodream.book.dto.BookCountDecreaseRequest;
import shop.dodream.book.dto.BookCountDecreaseResponse;
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

}