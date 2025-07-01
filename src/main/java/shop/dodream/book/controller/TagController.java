package shop.dodream.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tag", description = "태그 관리 API")
public class TagController {
    private final TagService tagService;

    // 태그 등록
    @Operation(summary = "태그 등록", description = "새로운 태그를 등록합니다.")
    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestParam String newTagName) {
        TagResponse response = tagService.createTag(newTagName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 태그 조회
    @Operation(summary = "전체 태그 조회", description = "모든 태그를 페이징 처리하여 조회합니다.")
    @GetMapping
    public Page<TagResponse> getTags(Pageable pageable) {
        return tagService.getTags(pageable);
    }

    // 태그 수정
    @Operation(summary = "태그 수정", description = "기존 태그를 새로운 이름으로 수정합니다.")
    @PutMapping("/{tag-id}")
    public TagResponse updateTag(@PathVariable("tag-id") Long tagId,
                                                 @RequestParam String newTagName) {
        return tagService.updateTag(tagId, newTagName);
    }

    // 태그 삭제
    @Operation(summary = "태그 삭제", description = "태그 ID로 태그를 삭제합니다.")
    @DeleteMapping("/{tag-id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("tag-id") Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
