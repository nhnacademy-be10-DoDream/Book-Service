package shop.dodream.book.infra.dto;


import lombok.Getter;
import lombok.Setter;
import shop.dodream.book.entity.Book;

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
        private String discount;
        private String publisher;
        private Date pubdate;
        private String isbn;


        public Book toPartialEntity() {
            return new Book(
                    title,
                    author,
                    Long.parseLong(discount),
                    publisher,
                    pubdate,
                    isbn
            );
        }
    }


}
