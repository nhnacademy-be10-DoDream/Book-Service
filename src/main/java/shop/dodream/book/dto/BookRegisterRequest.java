package shop.dodream.book.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.infra.dto.NaverBookResponse;
import shop.dodream.book.util.BookUtils;

import javax.swing.text.html.parser.Entity;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BookRegisterRequest {

    @NotBlank(message = "isbn 은 필수 값 입니다.")
    @Pattern(regexp = "\\d{13}", message = "ISBN은 13자리 숫자여야 합니다.")
    private String isbn;

    @NotBlank(message = "책 설명은 필수 값 입니다.")
    private String description;

    @Min(value = 0, message = "할인율은 0 이상이어야 합니다.")
    @Max(value = 50, message = "할인율은 50 이하이어야 합니다.")
    private Long discountRate;



    public void applyTo(Book book){
        long discountRate = this.discountRate;
        long regularPrice = (discountRate == 0) ? book.getSalePrice() : Math.round(book.getSalePrice() * 100.0 / (100 - discountRate));

        book.setDescription(this.description);
        book.setDiscountRate(discountRate);
        book.setRegularPrice(regularPrice);
        book.setStatus(BookStatus.SELL);
        book.setBookCount(50L);
        book.setSearchCount(0L);
        book.setViewCount(0L);
        book.setIsGiftable(true);
        book.setCreatedAt(ZonedDateTime.now());
    }


}
