package shop.dodream.book.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.dodream.book.command.ReviewUpdateCommand;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewUpdateRequest {

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Float rating;

    @NotBlank
    @Size(min = 5)
    private String content;

    private Set<String> images;

    public ReviewUpdateCommand toCommand(){
        if ((rating * 2) % 1 != 0) {
            throw new IllegalArgumentException("0 ~ 5 사이, 0.5 단위의 값을 사용해야 합니다");
        }

        byte ratingScale = (byte)(rating * 2);

        return ReviewUpdateCommand.builder()
                .rating(ratingScale)
                .content(content)
                .images(images)
                .build();
    }
}