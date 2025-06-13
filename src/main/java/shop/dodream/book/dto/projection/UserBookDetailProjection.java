package shop.dodream.book.dto.projection;

import java.time.LocalDate;

public interface UserBookDetailProjection {
    String getTitle();
    String getAuthor();
    String getDescription();
    String getPublisher();
    String getIsbn();
    LocalDate getPublishedAt();
    Long getSalePrice();
    Long getRegularPrice();
    Boolean getIsGiftable();
    String getBookUrl();
    Long getDiscountRate();

}
