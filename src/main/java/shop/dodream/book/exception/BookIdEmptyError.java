package shop.dodream.book.exception;

public class BookIdEmptyError extends RuntimeException {

  public BookIdEmptyError(Long bookId, String message) {
    super(bookId+message);

  }

}
