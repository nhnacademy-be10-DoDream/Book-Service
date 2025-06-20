package shop.dodream.book.repository;

import shop.dodream.book.dto.BookListResponse;

import java.util.List;

public interface BookLikeQuerydslRepository {
    List<BookListResponse> findLikedBooksByUserId(String userId);
}
