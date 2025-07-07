package shop.dodream.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.projection.BookListResponseRecord;


import java.util.List;

public interface BookLikeService {


    void registerBookLike(Long bookId, String userId);

    Boolean bookLikeFindMe(Long bookId, String userId);

    void bookLikeDelete(Long bookId, String userId);

    Page<BookListResponseRecord> getLikedBooksByUserId (String userId, Pageable pageable);

    Long getBookLikeCount(Long bookId);


}
