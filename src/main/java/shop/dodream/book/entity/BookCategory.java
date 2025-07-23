package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BookCategory extends BaseTimeEntity{

    @EmbeddedId
    private BookCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    @Getter
    private Book book;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @Getter
    @JoinColumn(name = "category_id")
    private Category category;


    public BookCategory(Book book, Category category) {
        this.book = book;
        this.category = category;
        this.id = new BookCategoryId(book.getId(), category.getId());
    }
}
