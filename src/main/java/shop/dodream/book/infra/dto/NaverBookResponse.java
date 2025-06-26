package shop.dodream.book.infra.dto;


import lombok.Getter;
import lombok.Setter;
import shop.dodream.book.entity.Book;
import shop.dodream.book.util.BookUtils;

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
            Book book = new Book();
            book.setTitle(title);
            book.setBookUrl(image);
            book.setAuthor(BookUtils.removeSpecialChars(author));
            book.setSalePrice(Long.parseLong(discount));
            book.setPublisher(publisher);
            book.setPublishedAt(pubdate);
            book.setIsbn(isbn);
            return book;
        }
    }




}
