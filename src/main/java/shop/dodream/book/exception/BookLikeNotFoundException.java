package shop.dodream.book.exception;

public class BookLikeNotFoundException extends NotFoundException {
    public BookLikeNotFoundException(Long bookId, String userId) {
        super("도서 ID [" + bookId + "]에 대한 사용자 [" + userId + "]의 좋아요 기록이 존재하지 않습니다.");
    }
}
