package shop.dodream.book.exception;

public class CategoryNameIsNullException extends RuntimeException {
    public CategoryNameIsNullException() {
        super("생성하고자 하는 카테고리 이름이 비어 있습니다.");
    }
}
