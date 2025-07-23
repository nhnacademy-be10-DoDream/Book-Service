package shop.dodream.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import shop.dodream.book.entity.BookStatus;


import java.util.Date;
import java.util.List;


@Data
@Document(indexName = "dodream_books_v2")
//@Setting(settingPath = "elasticsearch/setting.json")
//@Mapping(mappingPath = "elasticsearch/mapping.json")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class BookDocument {

    @Id
    @Field(type = FieldType.Long)
    private Long bookId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Keyword)
    private String publisher;

    @Field(type = FieldType.Long)
    private Long salePrice;

    @Field(type = FieldType.Date)
    private Date publishedAt;

    @Field(type = FieldType.Long)
    private Long viewCount;

    @Field(type = FieldType.Float)
    private Float ratingAvg;

    @Field(type = FieldType.Long)
    private Long reviewCount;

    @Field(type = FieldType.Text)
    private String uuid;

    @Field(type = FieldType.Long)
    private List<Long> categoryIds;

    @Field(type = FieldType.Keyword)
    private BookStatus status;

//    public BookDocument(Book book, String imageUrl) {
//        this.bookId = book.getId();
//        this.title = book.getTitle();
//        this.description = book.getDescription();
//        this.author = book.getAuthor();
//        this.publisher = book.getPublisher();
//        this.salePrice = book.getSalePrice();
//        this.publishedAt = Date.from(
//                book.getPublishedAt().atStartOfDay(ZoneId.systemDefault()).toInstant()
//        );
//        this.viewCount = book.getViewCount();
//        this.ratingAvg = 0.0f;
//        this.reviewCount = 0L;
//        this.imageUrl = imageUrl;
//        this.status = book.getStatus();
//    }


}
