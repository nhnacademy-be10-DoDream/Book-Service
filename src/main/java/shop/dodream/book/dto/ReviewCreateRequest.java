package shop.dodream.book.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.Review;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewCreateRequest {

    @NotNull
    @Min(0)
    @Max(5)
    private Double rating;

    @NotBlank
    @Size(min = 5)
    private String content;


    public Review toEntity(Book book, String userId) {
        if ((rating * 2) % 1 != 0) {
            throw new IllegalArgumentException("0 ~ 5 사이, 0.5 단위의 값을 사용해야 합니다");
        }

        byte ratingScale = (byte)(rating * 2);

        return new Review(
                ratingScale,
                content,
                userId,
                book
        );
    }
}