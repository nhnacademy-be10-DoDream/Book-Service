package shop.dodream.book.exception;

public class BookIdNotFoundException extends RuntimeException {
  public BookIdNotFoundException(String message) {
    super("Unauthorized");
  }
}
