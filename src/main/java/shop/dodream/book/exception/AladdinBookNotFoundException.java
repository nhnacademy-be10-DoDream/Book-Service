package shop.dodream.book.exception;

public class AladdinBookNotFoundException extends ResourceNotFoundException{
    public AladdinBookNotFoundException(String isbn) {
        super("ISBN [" + isbn + "] 에 해당하는 책 정보를 찾을 수 없습니다.");
    }
}
