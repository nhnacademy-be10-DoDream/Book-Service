package shop.dodream.book.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class IdsListRequest {
    @NotNull
    @NotEmpty(message = "ids 리스트는 비어 있을 수 없습니다.")
    List<Long> ids;
}
