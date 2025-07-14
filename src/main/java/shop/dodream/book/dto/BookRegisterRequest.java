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

    private String imageUrl;


    @AssertTrue(message = "할인가는 정가보다 작거나 같아야 합니다.")
    public boolean isSalePriceValid() {
        if (regularPrice == null || salePrice == null) return true;
        return salePrice <= regularPrice;
    }

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
