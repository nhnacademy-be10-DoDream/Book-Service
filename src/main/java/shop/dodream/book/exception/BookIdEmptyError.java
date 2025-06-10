package shop.dodream.book.exception;

public class BookIdEmptyError extends RuntimeException {
  public BookIdEmptyError(String message) {
    super("Unauthorized");
  }
}
