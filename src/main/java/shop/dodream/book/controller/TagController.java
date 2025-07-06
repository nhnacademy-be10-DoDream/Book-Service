package shop.dodream.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tag", description = "태그 관리 API")
public class TagController {
    private final TagService tagService;

    // 태그 등록
    @Operation(summary = "태그 등록", description = "새로운 태그를 등록합니다.")
    @PostMapping("/admin/tags")
    public ResponseEntity<TagResponse> createTag(@RequestParam String newTagName) {
        TagResponse response = tagService.createTag(newTagName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 단일 태그 조회
    @Operation(summary = "단일 태그 조회", description = "단일 태그를 조회합니다.")
    @GetMapping("/public/tags/{tag-id}")
    public TagResponse getTag(@PathVariable("tag-id") Long tagId) {
        return tagService.getTag(tagId);
    }

    // 전체 태그 조회
    @Operation(summary = "전체 태그 조회", description = "모든 태그를 조회합니다.")
    @GetMapping("/public/tags")
    public List<TagResponse> getTags() {
        return tagService.getTags();
    }

    // 태그 수정
    @Operation(summary = "태그 수정", description = "기존 태그를 새로운 이름으로 수정합니다.")
    @PutMapping("/admin/tags/{tag-id}")
    public TagResponse updateTag(@PathVariable("tag-id") Long tagId,
                                                 @RequestParam String newTagName) {
        return tagService.updateTag(tagId, newTagName);
    }

    // 태그 삭제
    @Operation(summary = "태그 삭제", description = "태그 ID로 태그를 삭제합니다.")
    @DeleteMapping("/admin/tags/{tag-id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("tag-id") Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
