package shop.dodream.book.exception;

public class BookTagDuplicateException extends ConflictException {
    public BookTagDuplicateException(Long bookId, Long tagId) {
        super("도서 [" + bookId + "] 에 태그 [" + tagId + "] 가 이미 존재합니다.");
    }
}
