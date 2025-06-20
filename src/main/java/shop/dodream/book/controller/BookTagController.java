package shop.dodream.book.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.*;
import shop.dodream.book.service.BookTagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookTagController {
    private final BookTagService bookTagService;


    @PostMapping("/admin/books/{book-id}/tags/{tag-id}")
    public ResponseEntity<BookWithTagResponse> registerTag(@PathVariable("book-id") Long bookId,
                                                                       @PathVariable("tag-id") Long tagId) {
        BookWithTagResponse response = bookTagService.registerTag(bookId, tagId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/books/{book-id}/tags")
    public ResponseEntity<BookWithTagsResponse> getTagsByBook(@PathVariable("book-id") Long bookId) {
        BookWithTagsResponse response = bookTagService.getTagsByBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/admin/books/{book-id}/tags/{tag-id}")
    public ResponseEntity<BookWithTagResponse> updateTagByBook(@PathVariable("book-id") Long bookId,
                                                               @PathVariable("tag-id") Long tagId,
                                                               @RequestBody @Valid BookWithTagRequest request) {
        BookWithTagResponse response = bookTagService.updateTagByBook(bookId, tagId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/admin/books/{book-id}/tags/{tag-id}")
    public ResponseEntity<Void> deleteTagByBook(@PathVariable("book-id") Long bookId,
                                                       @PathVariable("tag-id") Long tagId){
        bookTagService.deleteTagByBook(bookId, tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
