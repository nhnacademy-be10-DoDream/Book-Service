package shop.dodream.book.service;

import shop.dodream.book.dto.BookLikeCountResponse;
import shop.dodream.book.dto.BookLikeResponse;
import shop.dodream.book.dto.BookListResponse;

import java.util.List;

public interface BookLikeService {


    void registerBookLike(Long bookId, String userId);

    BookLikeResponse bookLikeFindMe(Long bookId, String userId);

    void bookLikeDelete(Long bookId, String userId);

    List<BookListResponse> getLikedBooksByUserId (String userId);


}
