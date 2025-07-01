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
    @Min(0)
    @Max(5)
    private Short rating;

    @NotBlank
    @Size(min = 5)
    private String content;

    private Set<String> images;
}