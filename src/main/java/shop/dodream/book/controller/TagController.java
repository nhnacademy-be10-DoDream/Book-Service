package shop.dodream.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.service.TagService;

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
    public Page<TagResponse> getTags(Pageable pageable) {
        return tagService.getTags(pageable);
    }

    // 태그 수정
    @PutMapping("/{tag-id}")
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
