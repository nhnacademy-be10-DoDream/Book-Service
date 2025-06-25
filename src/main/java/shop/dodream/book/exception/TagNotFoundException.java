package shop.dodream.book.exception;

public class TagNotFoundException extends ResourceNotFoundException {
    public TagNotFoundException(Long tagId) {
        super(tagId + " 라는 태그 아이디를 찾을 수 없습니다.");
    }
}
