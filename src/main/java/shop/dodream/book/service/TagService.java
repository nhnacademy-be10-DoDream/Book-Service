package shop.dodream.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.TagResponse;

public interface TagService {
    TagResponse createTag(String newTagName);
    Page<TagResponse> getTags(Pageable pageable);
    TagResponse updateTag(Long tagId, String newTagName);
    void deleteTag(Long tagId);
}
