package shop.dodream.book.exception;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException() {
        super("존재하지 않는 카테고리가 포함되어 있습니다.");
    }

    public CategoryNotFoundException(Long categoryId) {
        super("카테고리 ID " + categoryId + "를 찾을 수 없습니다.");
    }

    public CategoryNotFoundException(Long categoryId, Long parentId) {
        super(String.format("%d의 %d 라는 부모 카테고리를 찾을 수 없습니다.", categoryId, parentId));
    }
}
