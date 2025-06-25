package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.dto.BookUpdateRequest;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "Book", indexes = {
        @Index(name = "idx_sale_price", columnList = "salePrice"),
        @Index(name = "idx_created_at", columnList = "createdAt"),
        @Index(name = "idx_search_count", columnList = "searchCount"),
        @Index(name = "idx_view_count", columnList = "viewCount")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false) // 타입 text로 설정
    private String description;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private LocalDate publishedAt;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Long regularPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('SELL','SOLD_OUT','LOW_STOCK','REMOVED')")
    private BookStatus status;

    @Column(nullable = false)
    private Long salePrice;

    @Column(nullable = false)
    private Boolean isGiftable;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private Long searchCount;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Long bookCount;

    private String bookUrl;

    @Column(nullable = false)
    private Long discountRate;

    @Column(nullable = false)
    private Long likeCount;


    public void update(BookUpdateRequest bookUpdateRequest) {
        Optional.ofNullable(bookUpdateRequest.getTitle())
                .ifPresent(this::setTitle);

        Optional.ofNullable(bookUpdateRequest.getDescription())
                .ifPresent(this::setDescription);

        Optional.ofNullable(bookUpdateRequest.getAuthor())
                .ifPresent(this::setAuthor);

        Optional.ofNullable(bookUpdateRequest.getPublisher())
                .ifPresent(this::setPublisher);

        Optional.ofNullable(bookUpdateRequest.getRegularPrice())
                .ifPresent(this::setRegularPrice);

        Optional.ofNullable(bookUpdateRequest.getSalePrice())
                .ifPresent(this::setSalePrice);

        Optional.ofNullable(bookUpdateRequest.getIsGiftable())
                .ifPresent(this::setIsGiftable);

        Optional.ofNullable(bookUpdateRequest.getBookCount())
                .ifPresent(this::setBookCount);
    }



}
