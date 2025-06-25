package shop.dodream.book.service;

import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;

import java.util.List;

public interface BookTagService {
    BookWithTagResponse registerTag(Long bookId, Long tagId);
    BookWithTagsResponse getTagsByBook(Long bookId);
    List<BookListResponse> getBooksByTag(Long tagId);
    BookWithTagResponse updateTagByBook(Long BookId, Long tagId, Long newTagId);
    void deleteTagByBook(Long bookId, Long tagId);
}
