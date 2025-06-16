package shop.dodream.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.*;
import shop.dodream.book.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    // 태그 등록
    @PostMapping("/tags")
    public ResponseEntity<TagResponse> createTag(@RequestBody @Valid TagRequest request) {
        TagResponse response = tagService.createTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 태그 조회
    @GetMapping("/tags")
    public ResponseEntity<List<TagResponse>> getTags() {
        List<TagResponse> tags = tagService.getTags();
        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }

    // 해당 도서의 태그 조회
    @GetMapping("/tags/{tag-id}/books")
    public ResponseEntity<List<TagResponse>> getTagsByBookId(@PathVariable("tag-id") Long tagId) {
        List<TagResponse> response = tagService.getTagsByBook(tagId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 해당 태그의 도서 조회
    @GetMapping("/books/{book-id}/tags")
    public ResponseEntity<List<BookResponse>> getBooksByTagId(@PathVariable("book-id") Long bookId) {
        List<BookResponse> response = tagService.getBooksByTag(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 태그 수정
    @PatchMapping("/tags/{tag-id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable("tag-id") Long tagId,
                                                 @RequestBody @Valid TagRequest request) {
        TagResponse response = tagService.updateTag(tagId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 태그 삭제
    @DeleteMapping("/tags/{tag-id}")
    public ResponseEntity<CategoryResponse> deleteTag(@PathVariable("tag-id") Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
