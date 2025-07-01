package shop.dodream.book.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookTagService;


@RestController
@RequiredArgsConstructor
@Tag(name = "Book Tag", description = "도서 태그 관리 API")
public class BookTagController {
    private final BookTagService bookTagService;

    @Operation(summary = "도서에 태그 등록", description = "도서 ID와 태그 ID를 이용해 태그를 등록합니다.")
    @PostMapping("/books/{book-id}/tags/{tag-id}")
    public ResponseEntity<BookWithTagResponse> registerTag(@PathVariable("book-id") Long bookId,
                                                           @PathVariable("tag-id") Long tagId) {
        BookWithTagResponse response = bookTagService.registerTag(bookId, tagId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "도서의 태그 조회", description = "해당 도서에 등록된 태그를 조회합니다.")
    @GetMapping("/public/books/{book-id}/tags")
    public BookWithTagsResponse getTagsByBookId(@PathVariable("book-id") Long bookId) {
        return bookTagService.getTagsByBookId(bookId);
    }

    @Operation(summary = "태그 기준 도서 조회 (페이징)", description = "특정 태그가 등록된 도서를 조회합니다. 페이징 지원.")
    @GetMapping("/public/tags/{tag-id}/books")
    public Page<BookListResponseRecord> getBooksByTagId(@PathVariable("tag-id") Long tagId,
                                                        @PageableDefault(size = 10) Pageable pageable) {
        return bookTagService.getBooksByTagId(tagId, pageable);
    }

    @Operation(summary = "도서 태그 수정", description = "도서에 등록된 태그를 새로운 태그로 수정합니다.")
    @PutMapping("/books/{book-id}/tags/{tag-id}")
    public BookWithTagResponse updateTagByBook(@PathVariable("book-id") Long bookId,
                                                               @PathVariable("tag-id") Long tagId,
                                                               @RequestParam Long newTagId) {
        return bookTagService.updateTagByBook(bookId, tagId, newTagId);
    }

    @Operation(summary = "도서 태그 삭제", description = "도서에 등록된 태그를 삭제합니다.")
    @DeleteMapping("/books/{book-id}/tags/{tag-id}")
    public ResponseEntity<Void> deleteTagByBook(@PathVariable("book-id") Long bookId,
                                                       @PathVariable("tag-id") Long tagId){
        bookTagService.deleteTagByBook(bookId, tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
