package shop.dodream.book.dto;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookStatus;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Document(indexName = "books")
@NoArgsConstructor
public class BookDocument {
    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long bookId;

    @Field(type = FieldType.Text, analyzer = "korean_with_icu", searchAnalyzer = "korean_with_icu")
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String author;

    @Field(type = FieldType.Keyword)
    private String publisher;

    @Field(type = FieldType.Date)
    private LocalDate publishedAt;

    @Field(type = FieldType.Keyword)
    private String isbn;

    @Field(type = FieldType.Long)
    private Long regularPrice;

    @Field(type = FieldType.Keyword)
    private BookStatus status;

    @Field(type = FieldType.Long)
    private Long salePrice;

    @Field(type = FieldType.Boolean)
    private Boolean isGiftable;

    @Field(type = FieldType.Date)
    private ZonedDateTime createdAt;

    @Field(type = FieldType.Long)
    private Long bookCount;

    @Field(type = FieldType.Long)
    private Long discountRate;

    @Field(type = FieldType.Long)
    private Long salesCount;

    @Field(type = FieldType.Double)
    private Double rating;

    @Field(type = FieldType.Long)
    private Long reviewCount;


    public BookDocument(Book book) {
        this.id = String.valueOf(book.getId());
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.publishedAt = book.getPublishedAt();
        this.isbn = book.getIsbn();
        this.regularPrice = book.getRegularPrice();
        this.status = book.getStatus();
        this.salePrice = book.getSalePrice();
        this.isGiftable = book.getIsGiftable();
        this.createdAt = book.getCreatedAt();
        this.bookCount = book.getBookCount();
        this.discountRate = book.getDiscountRate();


//        this.salesCount = 0L;
//        this.rating = 0.0;
//        this.reviewCount = 0L;
    }

}
