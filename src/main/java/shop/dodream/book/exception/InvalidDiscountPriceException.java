package shop.dodream.book.exception;

public class InvalidDiscountPriceException extends RuntimeException {
    public InvalidDiscountPriceException(String message) {
        super(message);
    }
}
