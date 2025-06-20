package shop.dodream.book.exception;

public class CategoryIdNotFoundException extends NotFoundException {
    public CategoryIdNotFoundException(Long categoryId) {
        super("카테고리 ID " + categoryId + "를 찾을 수 없습니다.");
    }
}
