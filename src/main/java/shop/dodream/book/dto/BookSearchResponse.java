package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchResponse {
    private long totalHits;
    private int currentPage;
    private int totalPages;
    private List<BookItemResponse> books;

    public BookSearchResponse(long totalHits, List<BookItemResponse> books) {
        this.totalHits = totalHits;
        this.books = books;
    }

    public BookSearchResponse(long totalHits, List<BookItemResponse> books, int currentPage, int totalPages) {
        this.totalHits = totalHits;
        this.books = books;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }
}
