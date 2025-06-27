package shop.dodream.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import shop.dodream.book.dto.projection.ReviewStatsRecord;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.Review;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;


@Data
@Document(indexName = "dodream_books")
@Setting(settingPath = "elasticsearch/setting.json")
@Mapping(mappingPath = "elasticsearch/mapping.json")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private String publishedAt;

    @Field(type = FieldType.Long)
    private Long viewCount;

    @Field(type = FieldType.Float)
    private Float ratingAvg;

    @Field(type = FieldType.Long)
    private Long reviewCount;






    public BookDocument(Book book, ReviewStatsRecord reviewStatsRecord) {
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.salePrice = book.getSalePrice();
        this.publishedAt = book.getPublishedAt().toString();
        this.viewCount = book.getViewCount();
        this.ratingAvg = Optional.ofNullable(reviewStatsRecord.ratingAvg()).orElse(0.0f);
        this.reviewCount = reviewStatsRecord.reviewCount();

    }

}
