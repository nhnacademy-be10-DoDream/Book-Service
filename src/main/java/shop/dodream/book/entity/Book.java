package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

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
    private long salePrice;

    @Column(nullable = false)
    private Boolean isGiftable;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private long searchCount;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private long bookCount;

    private String bookUrl;

    private long discountRate;



}
