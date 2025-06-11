package shop.dodream.book.exception;

public class CategoryIdNotFoundException extends RuntimeException {
    public CategoryIdNotFoundException(Long categoryId, String message) {
        super(categoryId + " 라는 카테고리 아이디를 찾을 수 없습니다.");
    }
}
