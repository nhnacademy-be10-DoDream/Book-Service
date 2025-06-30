package shop.dodream.book.exception;

public class ElasticsearchBookNotFoundException extends ResourceNotFoundException {
    public ElasticsearchBookNotFoundException(Long bookId) {
        super("도서 ID " + bookId + "를 찾을 수 없습니다.");
    }
}
