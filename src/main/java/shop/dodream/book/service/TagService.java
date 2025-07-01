package shop.dodream.book.service;

import shop.dodream.book.dto.TagResponse;

import java.util.List;

public interface TagService {
    TagResponse createTag(String newTagName);
    List<TagResponse> getTags();
    TagResponse updateTag(Long tagId, String newTagName);
    void deleteTag(Long tagId);
}
