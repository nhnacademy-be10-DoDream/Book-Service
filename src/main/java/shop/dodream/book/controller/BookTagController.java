package shop.dodream.book.controller;


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
public class BookTagController {
    private final BookTagService bookTagService;


    @PostMapping("/books/{book-id}/tags/{tag-id}")
    public ResponseEntity<BookWithTagResponse> registerTag(@PathVariable("book-id") Long bookId,
                                                                       @PathVariable("tag-id") Long tagId) {
        BookWithTagResponse response = bookTagService.registerTag(bookId, tagId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/public/books/{book-id}/tags")
    public BookWithTagsResponse getTagsByBookId(@PathVariable("book-id") Long bookId) {
        return bookTagService.getTagsByBookId(bookId);
    }

    @GetMapping("/public/tags/{tag-id}/books")
    public Page<BookListResponseRecord> getBooksByTagId(@PathVariable("tag-id") Long tagId,
                                                        @PageableDefault(size = 10) Pageable pageable) {
        return bookTagService.getBooksByTagId(tagId, pageable);
    }

    @PutMapping("/books/{book-id}/tags/{tag-id}")
    public BookWithTagResponse updateTagByBook(@PathVariable("book-id") Long bookId,
                                                               @PathVariable("tag-id") Long tagId,
                                                               @RequestParam Long newTagId) {
        return bookTagService.updateTagByBook(bookId, tagId, newTagId);
    }

    @DeleteMapping("/books/{book-id}/tags/{tag-id}")
    public ResponseEntity<Void> deleteTagByBook(@PathVariable("book-id") Long bookId,
                                                       @PathVariable("tag-id") Long tagId){
        bookTagService.deleteTagByBook(bookId, tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
