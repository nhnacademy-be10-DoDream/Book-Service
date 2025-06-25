package shop.dodream.book.exception;

public class InvalidParentCategoryException extends IllegalArgumentException {
    public InvalidParentCategoryException() {
        super("자기 자신을 부모 카테고리로 지정할 수 없습니다.");
    }

    public InvalidParentCategoryException(String message) {
        super(message);
    }
}
