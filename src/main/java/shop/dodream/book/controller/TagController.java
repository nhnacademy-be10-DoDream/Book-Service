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
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    // 태그 등록
    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestParam String newTagName) {
        TagResponse response = tagService.createTag(newTagName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 태그 조회
    @GetMapping
    public List<TagResponse> getTags() {
        return tagService.getTags();
    }

    // 태그 수정
    @PatchMapping("/{tag-id}")
    public TagResponse updateTag(@PathVariable("tag-id") Long tagId,
                                                 @RequestParam String newTagName) {
        return tagService.updateTag(tagId, newTagName);
    }

    // 태그 삭제
    @DeleteMapping("/{tag-id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("tag-id") Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
