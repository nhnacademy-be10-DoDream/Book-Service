package shop.dodream.book.infra.dto;


import lombok.Getter;
import lombok.Setter;
import shop.dodream.book.entity.Book;
import shop.dodream.book.util.BookUtils;

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


        public Book toPartialEntity() {
            Book book = new Book();
            book.setTitle(this.title);
//            book.setBookUrl(this.image);
            book.setAuthor(BookUtils.removeSpecialChars(this.author));
            book.setSalePrice(Long.parseLong(this.discount));
            book.setPublisher(this.publisher);
            book.setPublishedAt(BookUtils.parseDate(this.pubdate));
            book.setIsbn(this.isbn);
            return book;
        }
    }




}
