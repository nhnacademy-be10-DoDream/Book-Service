package shop.dodream.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class IdsListRequest {
    @NotNull
    List<Long> ids;
}
