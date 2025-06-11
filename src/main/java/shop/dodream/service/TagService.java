package shop.dodream.service;

import shop.dodream.book.dto.*;

import java.util.List;

public interface TagService {
    TagResponse createTag(TagRequest request);
    List<TagResponse> getTags();
    List<TagResponse> getTagsByBook(Long bookId);
    List<BookResponse> getBooksByTag(Long tagId);
    TagResponse updateTag(Long tagId, TagRequest request);
    void deleteTag(Long tagId);
}
