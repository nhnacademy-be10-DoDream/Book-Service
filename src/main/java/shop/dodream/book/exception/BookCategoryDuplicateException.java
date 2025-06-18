package shop.dodream.book.exception;

public class BookCategoryDuplicateException extends ConflictException {
  public BookCategoryDuplicateException(Long bookId, Long categoryId) {
    super("도서 ID [" + bookId + "] 는 이미 카테고리 ID [" + categoryId + "] 와 매핑되어 있습니다.");
  }
}
