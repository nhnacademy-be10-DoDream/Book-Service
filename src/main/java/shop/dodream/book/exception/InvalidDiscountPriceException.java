package shop.dodream.book.exception;

public class InvalidDiscountPriceException extends BadRequestException {
    public InvalidDiscountPriceException(Long salePrice, Long regularPrice) {
        super("할인가("+salePrice+")가 정가("+regularPrice+")보다 높을 순 없습니다.");
    }
}
