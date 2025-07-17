package shop.dodream.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookSortType;
import shop.dodream.book.service.BookSearchService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/public/books")
@Tag(name = "Book Search", description = "도서 검색 API")
public class BookSearchController {
    private final BookSearchService bookSearchService;

    @Operation(summary = "도서 검색", description = "키워드를 통해 도서를 검색합니다. 정렬 옵션을 선택할 수 있습니다.")
    @GetMapping("/search")
    public Page<BookItemResponse> searchBooks(@RequestParam String keyword,
                                              @RequestParam(value = "sort", required = false, defaultValue = "NONE") BookSortType sort, Pageable pageable,
                                              @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds,
                                              @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                              @RequestParam(value = "maxPrice", required = false) Integer maxPrice) {
        return bookSearchService.searchBooks(keyword, sort, pageable, categoryIds, minPrice, maxPrice);
    }
}
