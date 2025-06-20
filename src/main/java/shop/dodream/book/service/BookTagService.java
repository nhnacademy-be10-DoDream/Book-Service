package shop.dodream.book.service;

import shop.dodream.book.dto.BookWithTagRequest;
import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;

import java.util.List;

public interface BookTagService {
    BookWithTagResponse registerTag(Long bookId, Long tagId);
    BookWithTagsResponse getTagsByBook(Long bookId);
    BookWithTagResponse updateTagByBook(Long BookId, Long tagId, BookWithTagRequest request);
    void deleteTagByBook(Long bookId, Long tagId);
}
