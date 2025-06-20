package shop.dodream.book.service;

import shop.dodream.book.dto.*;

import java.util.List;

public interface TagService {
    TagResponse createTag(TagRequest request);
    List<TagResponse> getTags();
    TagResponse updateTag(Long tagId, TagRequest request);
    void deleteTag(Long tagId);
}
