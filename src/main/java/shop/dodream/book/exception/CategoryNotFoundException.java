package shop.dodream.book.exception;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException() {
        super("존재하지 않는 카테고리가 포함되어 있습니다.");
    }
}
