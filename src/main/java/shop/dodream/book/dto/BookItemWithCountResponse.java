package shop.dodream.book.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.Map;

@Getter
public class BookItemWithCountResponse {
    private final Page<BookItemResponse> books;
    private final Map<Long, Long> categoryCountMap;

    public BookItemWithCountResponse(Page<BookItemResponse> books, Map<Long, Long> categoryCountMap) {
        this.books = books;
        this.categoryCountMap = categoryCountMap;
    }

}
