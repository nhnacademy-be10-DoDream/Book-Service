package shop.dodream.book.service;

import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;

public interface BookLikeService {


    void registerBookLike(Long bookId, String userId);

    Boolean bookLikeFindMe(Long bookId, String userId);

    void bookLikeDelete(Long bookId, String userId);

    List<BookListResponseRecord> getLikedBooksByUserId (String userId);

    Long getBookLikeCount(Long bookId);


}
