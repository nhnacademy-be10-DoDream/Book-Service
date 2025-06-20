package shop.dodream.book.exception;

public class CategoryMissingException extends BadRequestException {
  public CategoryMissingException() {
    super("최소 한 개 이상의 카테고리를 등록해야 합니다.");
  }
}
