package shop.dodream.book.exception;

public class BookNotOrderableException extends ConflictException  {
    public BookNotOrderableException() {
        super("해당 도서는 주문할 수없는 상태입니다.");
    }
}
