package shop.dodream.book.repository;

import shop.dodream.book.dto.AdminBookDetailResponse;
import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.dto.UserBookDetailResponse;

import java.util.List;
import java.util.Optional;

public interface BookQuerydslRepository {
    List<BookListResponse> findAllBy();

    Optional<UserBookDetailResponse> findBookDetailForUserById(Long bookId);




}
