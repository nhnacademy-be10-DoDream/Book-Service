package shop.dodream.book.exception;

public class BookLikeIdNotFoundException extends RuntimeException {
    public BookLikeIdNotFoundException(String message) {
        super(message);
    }
}
