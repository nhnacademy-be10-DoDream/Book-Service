package shop.dodream.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class IdsListRequest {
    @NotNull
    @Size(min = 1, max = 10)
    List<@NotBlank Long> ids;
}
