package shop.dodream.book.exception;

public class BookCountNotEnoughException extends RuntimeException {
    public BookCountNotEnoughException(String message) {
        super(message);
    }
}
