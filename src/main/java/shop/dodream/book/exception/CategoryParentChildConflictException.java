package shop.dodream.book.exception;

public class CategoryParentChildConflictException extends RuntimeException {
    public CategoryParentChildConflictException(Long childId, Long parentId) {
        super(String.format("카테고리%d는 부모 카테고리 %d와 동시에 등록할 수 없습니다.", childId, parentId));
    }
}
