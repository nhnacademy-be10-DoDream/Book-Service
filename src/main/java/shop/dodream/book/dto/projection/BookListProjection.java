package shop.dodream.book.dto.projection;

import shop.dodream.book.entity.BookStatus;

import java.time.ZonedDateTime;

public interface BookListProjection {
    Long getId();
    String getTitle();
    String getAuthor();
    String getIsbn();
    BookStatus getStatus();
    Long getRegularPrice();
    Long getSalePrice();
    Boolean getIsGiftable();
    Long getViewCount();
    Long getSearchCount();
    ZonedDateTime getCreatedAt();
}
