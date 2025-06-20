package shop.dodream.book.exception;

public class CategoryDepthNotFoundException extends NotFoundException {
    public CategoryDepthNotFoundException(Long depth) {
        super("깊이 " + depth + "에 해당하는 카테고리를 찾을 수 없습니다.");
    }
}
