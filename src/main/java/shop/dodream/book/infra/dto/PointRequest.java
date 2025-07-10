package shop.dodream.book.infra.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointRequest {
    @NotNull
    @Min(0)
    Long amount;

    @NotNull
    String pointType;

    @NotNull
    String policyType;

    Boolean isPolicyCommon;

    @Size(max = 255)
    String description;
}