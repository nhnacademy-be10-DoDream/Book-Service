package shop.dodream.book.infra.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AladdinBookResponse {
    private Integer errorCode;
    private String errorMessage;
    private Integer totalResults;
    private List<Item> item;


    @Getter
    @Setter
    public static class Item{
        private String title;
        private String description;
        private String author;
        private String publisher;
        private String isbn13;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate pubDate;
        private Long priceStandard; // 정가
        private Long priceSales; // 판매가
        private String cover; //이미지 url


    }



}
