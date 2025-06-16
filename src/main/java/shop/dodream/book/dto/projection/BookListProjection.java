package shop.dodream.book.dto.projection;


import java.time.ZonedDateTime;

public interface BookListProjection {
    Long getId();
    String getTitle();
    String getAuthor();
    String getIsbn();
    Long getRegularPrice();
    Long getSalePrice();
}
