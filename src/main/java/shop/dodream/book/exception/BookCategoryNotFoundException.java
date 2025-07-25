package shop.dodream.book.exception;

public class BookCategoryNotFoundException extends ResourceNotFoundException {
    public BookCategoryNotFoundException(Long bookId, Long categoryId) {
        super("도서 ID " + bookId + "에 연결된 카테고리가 존재하지 않음: " + categoryId);
    }
}
