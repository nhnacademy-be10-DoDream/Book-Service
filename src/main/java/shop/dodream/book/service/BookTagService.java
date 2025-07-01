package shop.dodream.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;

public interface BookTagService {
    BookWithTagResponse registerTag(Long bookId, Long tagId);
    BookWithTagsResponse getTagsByBookId(Long bookId);
    Page<BookListResponseRecord> getBooksByTagId(Long tagId, Pageable pageable);
    BookWithTagResponse updateTagByBook(Long bookId, Long tagId, Long newTagId);
    void deleteTagByBook(Long bookId, Long tagId);
}
