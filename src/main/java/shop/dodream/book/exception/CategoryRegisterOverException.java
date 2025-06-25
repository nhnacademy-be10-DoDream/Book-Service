package shop.dodream.book.exception;

public class CategoryRegisterOverException extends IllegalArgumentException {
    public CategoryRegisterOverException() {
        super("책은 최대 10개의 카테고리에만 속할 수 있습니다.");
    }
}
