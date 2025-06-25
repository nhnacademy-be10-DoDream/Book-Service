package shop.dodream.book.exception;

public class BookTagNotFoundException extends ResourceNotFoundException {
    public BookTagNotFoundException(Long bookId, Long tagId) {
        super("도서 ID가 " + bookId + "이고 태그 ID가 " + tagId + "인 태그 연결 정보를 찾을 수 없습니다.");
    }
}
