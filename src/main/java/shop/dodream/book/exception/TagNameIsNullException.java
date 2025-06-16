package shop.dodream.book.exception;

public class TagNameIsNullException extends NotFoundException {
    public TagNameIsNullException() {
        super("생성하고자 하는 태그 이름이 비어 있습니다.");
    }
}
