package shop.dodream.book.exception;

public class InvalidDiscountPriceException extends ConflictException {
    public InvalidDiscountPriceException(String message) {
        super(message);
    }
}
