package shop.dodream.book.exception;

public class ReviewNotFoundException extends ResourceNotFoundException {
    public ReviewNotFoundException(Long reviewId) {
        super(String.format("해당 리뷰를 찾을 수 없습니다. (reviewId : %d)", reviewId));
    }
}