package shop.dodream.book.exception;

public class CategoryParentIdNotFoundException extends RuntimeException {
    public CategoryParentIdNotFoundException(Long parentId, String message) {
        super(parentId + message);
    }
}
