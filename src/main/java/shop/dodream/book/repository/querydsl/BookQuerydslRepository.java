package shop.dodream.book.repository.querydsl;

import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.dto.UserBookDetailResponse;

import java.util.List;
import java.util.Optional;

public interface BookQuerydslRepository {
    List<BookListResponse> findAllBy();

    Optional<UserBookDetailResponse> findBookDetailForUserById(Long bookId);

//    void incrementLikCount(Long bookId);
//
//    void decreaseLikeCount(Long bookId);

//    Optional<BookLikeCountResponse> findLikeCountByBookId(Long bookId);

    List<BookListResponse> findVisibleBooksByIds(List<Long> ids);
}
