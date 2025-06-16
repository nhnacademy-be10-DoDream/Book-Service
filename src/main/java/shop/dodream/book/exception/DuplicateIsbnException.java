package shop.dodream.book.exception;

public class DuplicateIsbnException extends ConflictException{
    public DuplicateIsbnException(String isbn) {
        super("이미 등록된 ISBN입니다: " + isbn);

    }
}
