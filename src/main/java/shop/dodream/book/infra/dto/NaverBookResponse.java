package shop.dodream.book.infra.dto;


import co.elastic.clients.util.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import shop.dodream.book.entity.Book;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class NaverBookResponse {
    private List<Item> items;


    @Getter
    @Setter
    public static class Item{
        private String title;
        private String image;
        private String author;
        private Long discount;
        private String publisher;
        @JsonFormat(pattern = "yyyyMMdd")
        private LocalDate pubdate;
        private String isbn;


        public Book toPartialEntity() {
            return new Book(
                    title,
                    author,
                    discount,
                    publisher,
                    pubdate,
                    isbn
            );
        }
    }


}
