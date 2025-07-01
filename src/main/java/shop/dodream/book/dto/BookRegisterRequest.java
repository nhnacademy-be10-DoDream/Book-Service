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


    @NotBlank
    private String title;

    @NotBlank(message = "책 설명은 필수 값 입니다.")
    private String description;

    @NotBlank
    private String author;

    @NotBlank
    private String publisher;

    @NotNull
    private LocalDate publishedAt;

    @NotBlank(message = "isbn 은 필수 값 입니다.")
    @Pattern(regexp = "\\d{13}", message = "ISBN은 13자리 숫자여야 합니다.")
    private String isbn;


    @NotNull
    @Positive
    private Long regularPrice;

    @NotNull
    @PositiveOrZero
    private Long salePrice;

    @NotNull
    private Boolean isGiftable;

    @NotNull
    @Min(5)
    @Max(500)
    private Long bookCount;

    @AssertTrue(message = "할인가는 정가보다 작거나 같아야 합니다.")
    public boolean isSalePriceValid() {
        if (regularPrice == null || salePrice == null) return true;
        return salePrice <= regularPrice;
    }


//    @Min(value = 0, message = "할인율은 0 이상이어야 합니다.")
//    @Max(value = 50, message = "할인율은 50 이하이어야 합니다.")
//    private Long discountRate;



//    public Book toEntity(AladdinBookResponse aladdinBookResponse){
//
//        AladdinBookResponse.Item bookItem = aladdinBookResponse.getItem().getFirst();
//
//        long regularPrice = aladdinBookResponse.getItem().getFirst().getPriceStandard();
//        long salePrice = aladdinBookResponse.getItem().getFirst().getPriceSales();
//
//        // 할인율 계산
//        long discountRate = 0L;
//        if (regularPrice > 0 && salePrice <= regularPrice) {
//            discountRate = (long) ((double) (regularPrice - salePrice) / regularPrice * 100);
//        }
//
//        return new Book(
//                bookItem.getTitle(),
//                bookItem.getDescription(),
//                bookItem.getAuthor(),
//                bookItem.getPublisher(),
//                bookItem.getPubDate(),
//                isbn,
//                regularPrice,
//                BookStatus.SELL,
//                salePrice,
//                true,
//                0L,
//                50L,
//                discountRate
//        );
//
//    }

//    public Book toEntity(){
//        return new Book(
//                title,
//                description,
//                author,
//                publisher,
//                publishedAt,
//                isbn,
//                regularPrice,
//                isGiftable,
//                salePrice,
//                bookCount
//        )
//    }


    public Book toEntity() {
        return new Book(
                title,
                description,
                author,
                publisher,
                publishedAt,
                isbn,
                regularPrice,
                BookStatus.SELL,
                salePrice,
                isGiftable,
                0L,
                bookCount

        );
    }





}
