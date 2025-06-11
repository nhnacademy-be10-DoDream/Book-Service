package shop.dodream.book.infra.dto;


import lombok.Getter;
import lombok.Setter;

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
        private String pubdate;
        private String isbn;
    }

}
