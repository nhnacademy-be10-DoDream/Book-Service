package shop.dodream.book.infra.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AladdinBookResponse {
    private Integer errorCode;
    private String errorMessage;
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

        public Book toEntity(){

            // 할인율 계산
            long discountRate = 0L;
            if (priceStandard > 0 && priceSales <= priceStandard) {
                discountRate = Math.round((1 - (double) priceSales / priceStandard) * 100);

            }

            return new Book(
                    title,
                    description,
                    author,
                    publisher,
                    pubDate,
                    isbn13,
                    priceStandard,
                    BookStatus.SELL,
                    priceSales,
                    true,
                    0L,
                    50L,
                    discountRate
            );
        }


    }



}
