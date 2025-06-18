package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor

public class BookTag {

    @EmbeddedId
    private BookTagId id;


    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public BookTag(Book book, Tag tag) {
        this.book = book;
        this.tag = tag;
        this.id = new BookTagId(book.getId(), tag.getId());
    }
}
