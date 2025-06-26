package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.dto.BookUpdateRequest;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(columnDefinition = "TEXT", nullable = false) // 타입 text로 설정
    private String description;

    @Setter
    @Column(nullable = false)
    private String author;

    @Setter
    @Column(nullable = false)
    private String publisher;

    @Setter
    @Column(nullable = false)
    private Date publishedAt;

    @Setter
    @Column(unique = true, nullable = false)
    private String isbn;

    @Setter
    @Column(nullable = false)
    private Long regularPrice;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @Setter
    @Column(nullable = false)
    private Long salePrice;

    @Setter
    @Column(nullable = false)
    private Boolean isGiftable;

    @Setter
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Setter
    @Column(nullable = false)
    private Long searchCount;

    @Setter
    @Column(nullable = false)
    private Long viewCount;

    @Setter
    @Column(nullable = false)
    private Long bookCount;

    @Setter
    private String bookUrl;

    @Setter
    @Column(nullable = false)
    private Long discountRate;

    @Setter
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
