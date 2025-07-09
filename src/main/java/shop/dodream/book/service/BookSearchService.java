package shop.dodream.book.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookSortType;




public interface BookSearchService {
    Page<BookItemResponse> searchBooks(String keyword, BookSortType bookSortType, Pageable pageable);

}
