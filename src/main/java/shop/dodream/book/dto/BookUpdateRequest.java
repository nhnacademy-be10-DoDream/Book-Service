package shop.dodream.book.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class BookUpdateRequest {

    private String title;
    private String description;
    private String author;
    private String publisher;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishedAt;

    @Min(value = 0, message = "정가는 0이상이어야합니다.")
    private Long regularPrice; // 정가

    @Min(value = 0, message = "할인가는 0이상이어야합니다.")
    private Long salePrice; // 할인가

    private Boolean isGiftable;

    @Min(value = 0, message = "수량은 0이상이어야합니다.")
    private Long bookCount;


    @AssertTrue(message = "할인가는 정가보다 작거나 같아야 합니다.")
    public boolean isSalePriceValid() {
        if (regularPrice == null || salePrice == null) return true;
        return salePrice <= regularPrice;
    }




    public Map<String, Object> toUpdateMap(){
        Map<String, Object> map = new HashMap<>();
        if (title != null) map.put("title", title);
        if (description != null) map.put("description", description);
        if (author != null) map.put("author", author);
        if (publisher != null) map.put("publisher", publisher);
        if (salePrice != null) map.put("salePrice", salePrice);
        if (publishedAt != null) map.put("publishedAt", Date.from(
                publishedAt.atStartOfDay(ZoneId.systemDefault()).toInstant()
        ));
        return map;
    }

}
