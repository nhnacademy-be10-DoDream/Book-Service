package shop.dodream.book.exception;

public class BookNotOrderableException extends ResourceConflictException  {
    public BookNotOrderableException() {
        super("해당 도서는 주문할 수 없는 상태입니다.");
    }
}
