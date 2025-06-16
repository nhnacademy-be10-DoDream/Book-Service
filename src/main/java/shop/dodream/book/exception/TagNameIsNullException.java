package shop.dodream.book.exception;

public class TagNameIsNullException extends NotFoundException {
    public TagNameIsNullException(String message) {
        super(message);
    }
}
