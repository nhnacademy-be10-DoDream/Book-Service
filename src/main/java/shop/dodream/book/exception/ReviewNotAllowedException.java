package shop.dodream.book.exception;

public class ReviewNotAllowedException extends ForbiddenException {
    public ReviewNotAllowedException(String userId) {
        super(String.format("구매한 도서에 대해서만 리뷰를 작성할 수 있습니다. (userId : %s)", userId));
    }
}
