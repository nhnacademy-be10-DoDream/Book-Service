package shop.dodream.book.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.Image;
import shop.dodream.book.infra.dto.AladdinBookResponse;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class BookRegisterRequest {


//    @NotBlank(message = "책 설명은 필수 값 입니다.")
//    private String description;


    @NotBlank(message = "isbn 은 필수 값 입니다.")
    @Pattern(regexp = "\\d{13}", message = "ISBN은 13자리 숫자여야 합니다.")
    private String isbn;


//    @Min(value = 0, message = "할인율은 0 이상이어야 합니다.")
//    @Max(value = 50, message = "할인율은 50 이하이어야 합니다.")
//    private Long discountRate;



    public Book toEntity(AladdinBookResponse aladdinBookResponse){

        AladdinBookResponse.Item bookItem = aladdinBookResponse.getItem().getFirst();

        long regularPrice = aladdinBookResponse.getItem().getFirst().getPriceStandard();
        long salePrice = aladdinBookResponse.getItem().getFirst().getPriceSales();

        // 할인율 계산
        long discountRate = 0L;
        if (regularPrice > 0 && salePrice <= regularPrice) {
            discountRate = (long) ((double) (regularPrice - salePrice) / regularPrice * 100);
        }

        return new Book(
                bookItem.getTitle(),
                bookItem.getDescription(),
                bookItem.getAuthor(),
                bookItem.getPublisher(),
                bookItem.getPubDate(),
                isbn,
                regularPrice,
                BookStatus.SELL,
                salePrice,
                true,
                0L,
                50L,
                discountRate
        );

    }




}
