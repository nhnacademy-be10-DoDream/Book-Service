package shop.dodream.book.exception;

public class BookAlreadyRemovedException extends ResourceConflictException {
    public BookAlreadyRemovedException() {
        super("삭제된 도서는 수정할수 없습니다");
    }
}
