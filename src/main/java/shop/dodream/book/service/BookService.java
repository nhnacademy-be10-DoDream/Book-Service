package shop.dodream.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookAdminListResponseRecord;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;

public interface BookService {

    void registerBookDirect(BookRegisterRequest registerRequest, List<MultipartFile> files);

    AladdinBookSearchResult getAladdinBookList(String query, int size, int page);

    void registerFromAladdin(BookRegisterRequest request);

    Page<BookAdminListResponseRecord> getAllBooks(Pageable pageable);

    List<BookListResponseRecord> getAllBooks();

    List<BookListResponseRecord> findAllByIds(List<Long> ids);

    BookDetailResponse getBookByIdForAdmin(Long bookId);

    BookDetailResponse getBookByIdForUser(Long bookId);

    void updateBook(Long bookId, BookUpdateRequest request, List<MultipartFile> files);

    void deleteBook(Long bookId);

    BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request);

    void increaseBookCount(BookCountIncreaseRequest request);

    BookItemResponse getBookByIsbn(String isbn);

    void redisIncreaseViewCount(Long bookId);

}
