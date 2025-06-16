package shop.dodream.book.exception;

public class BookLikeAlreadyRegisterException extends ConflictException {
    public BookLikeAlreadyRegisterException() {
        super("이미 좋아요를 눌른 도서입니다.");
    }
}
