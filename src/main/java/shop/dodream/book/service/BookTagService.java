package shop.dodream.book.service;

import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;

public interface BookTagService {
    BookWithTagResponse registerTag(Long bookId, Long tagId);
    BookWithTagsResponse getTagsByBookId(Long bookId);
    List<BookListResponseRecord> getBooksByTagId(Long tagId);
    BookWithTagResponse updateTagByBook(Long bookId, Long tagId, Long newTagId);
    void deleteTagByBook(Long bookId, Long tagId);
}
