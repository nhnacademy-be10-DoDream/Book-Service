package shop.dodream.book.exception;

public class BookNotFoundException extends ResourceNotFoundException {
  public BookNotFoundException(Long bookId) {
    super("해당하는 ID("+bookId+") 의 책은 존재하지 않습니다.");
  }
}
