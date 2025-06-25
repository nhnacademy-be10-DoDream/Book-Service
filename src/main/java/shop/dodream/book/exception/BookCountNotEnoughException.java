package shop.dodream.book.exception;

public class BookCountNotEnoughException extends ResourceConflictException {
    public BookCountNotEnoughException(Long currentStock) {
        super("재고가 부족합니다. 현재 재고: "+currentStock);
    }
}