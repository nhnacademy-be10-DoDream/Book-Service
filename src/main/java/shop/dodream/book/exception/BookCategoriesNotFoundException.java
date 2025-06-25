package shop.dodream.book.exception;

import java.util.List;

public class BookCategoriesNotFoundException extends ResourceNotFoundException {
    public BookCategoriesNotFoundException(Long bookId, List<Long> categoryIds) {
        super("도서 ID " + bookId + "에 연결된 카테고리가 존재하지 않음: " + categoryIds);
    }
}
