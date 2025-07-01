package shop.dodream.book.service;

import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;

import java.util.List;

public interface BookService {

    void registerBookByIsbn(String isbn);

    void registerBookDirect(BookRegisterRequest registerRequest, List<MultipartFile> files);


    List<BookListResponseRecord> getAllBooks();

    List<BookListResponseRecord> findAllByIds(List<Long> ids);

    BookDetailResponse getBookByIdForAdmin(Long bookId);

    BookDetailResponse getBookByIdForUser(Long bookId);

    void updateBook(Long bookId, BookUpdateRequest request, List<MultipartFile> files);

    void deleteBook(Long bookId);

    BookCountDecreaseResponse decreaseBookCount(BookCountDecreaseRequest request);

}
