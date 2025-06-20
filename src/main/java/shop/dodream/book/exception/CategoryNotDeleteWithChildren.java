package shop.dodream.book.exception;

public class CategoryNotDeleteWithChildren extends RuntimeException {
    public CategoryNotDeleteWithChildren(Long categoryId) {
        super("ID가 " + categoryId + "인 카테고리는 하위 카테고리가 존재하므로 삭제할 수 없습니다.");
    }
}
