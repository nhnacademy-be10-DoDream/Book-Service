package shop.dodream.book.exception;

public class BookAlreadyRemovedException extends RuntimeException {
    public BookAlreadyRemovedException(String message) {
        super(message);
    }
}
