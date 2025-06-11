package shop.dodream.book.exception;

public class CategoryParentIdNotFoundException extends RuntimeException {
    public CategoryParentIdNotFoundException(Long parentId) {
        super(parentId + " 라는 부모 카테고리를 찾을 수 없습니다.");
    }
}
