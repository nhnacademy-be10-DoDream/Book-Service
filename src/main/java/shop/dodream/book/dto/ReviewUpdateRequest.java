package shop.dodream.book.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}