package shop.dodream.book.exception;

public class BookNotOrderableException extends RuntimeException  {
    public BookNotOrderableException(String message) {
        super(message);
    }
}
