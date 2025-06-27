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
    private Short rating;

    @NotBlank
    @Size(min = 5)
    private String content;


    public Review toEntity(Book book, String userId) {



        return new Review(
                rating,
                content,
                userId,
                book
        );
    }
}