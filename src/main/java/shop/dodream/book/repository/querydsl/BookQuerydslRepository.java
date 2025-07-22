package shop.dodream.book.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.projection.BookAdminListResponseRecord;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;
import java.util.Optional;

public interface BookQuerydslRepository {
    Page<BookAdminListResponseRecord> findAllBy(Pageable pageable);

    List<BookListResponseRecord> findAllBy();

    List<BookListResponseRecord> findVisibleBooksByIds(List<Long> ids);

    Optional<BookDetailResponse> findBookDetailForUser(Long bookId);
    Optional<BookDetailResponse> findBookDetailForAdmin(Long bookId);


    Optional<BookItemResponse> findByIsbn(String Isbn);


    void incrementViewCount(Long bookId, Long increment);


}
