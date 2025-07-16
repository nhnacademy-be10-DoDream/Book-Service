package shop.dodream.book.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.infra.dto.AladdinBookResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookItemResponse {
    private Long bookId;
    private String title;
    private String description;
    private String author;
    private String publisher;
    private String isbn;
    private String publishedAt;
    private Long regularPrice;
    private Long salePrice;
    private String imageUrl;
    private List<Long> categoryIds;


    public BookItemResponse(BookDocument document) {
        this.bookId = document.getBookId();
        this.title = document.getTitle();
        this.description = document.getDescription();
        this.author = document.getAuthor();
        this.publisher = document.getPublisher();
        this.salePrice = document.getSalePrice();
        this.publishedAt = String.valueOf(document.getPublishedAt());
        this.imageUrl = document.getImageUrl();
        this.categoryIds = document.getCategoryIds();
    }

    public static BookItemResponse from(AladdinBookResponse.Item item) {
        BookItemResponse response = new BookItemResponse();
        response.setTitle(item.getTitle());
        response.setDescription(item.getDescription());
        response.setAuthor(item.getAuthor());
        response.setPublisher(item.getPublisher());
        response.setIsbn(item.getIsbn13());
        response.setPublishedAt(String.valueOf(item.getPubDate()));
        response.setRegularPrice(item.getPriceStandard());
        response.setSalePrice(item.getPriceSales());
        response.setImageUrl(item.getCover());
        return response;
    }
}


