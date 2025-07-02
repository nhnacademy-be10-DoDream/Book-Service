package shop.dodream.book.repository.querydsl;

import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;
import java.util.Optional;

public interface BookQuerydslRepository {
    List<BookListResponseRecord> findAllBy();

    List<BookListResponseRecord> findVisibleBooksByIds(List<Long> ids);

    Optional<BookDetailResponse> findBookDetailForAdmin(Long bookId);
    Optional<BookDetailResponse> findBookDetailForUser(Long bookId);


    Optional<BookResponse> findByIsbn(String Isbn);
}
