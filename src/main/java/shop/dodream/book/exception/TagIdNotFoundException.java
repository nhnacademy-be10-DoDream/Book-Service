package shop.dodream.book.exception;

public class TagIdNotFoundException extends RuntimeException {
    public TagIdNotFoundException(Long tagId, String message) {
        super(tagId + message);
    }
}
