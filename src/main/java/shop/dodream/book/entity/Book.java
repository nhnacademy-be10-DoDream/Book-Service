package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.dto.BookUpdateRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class Book extends BaseTimeEntity{

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
    private LocalDate publishedAt;

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
    private Long searchCount;

    @Setter
    @Column(nullable = false)
    private Long viewCount;

    @Setter
    @Column(nullable = false)
    private Long bookCount;

    @Getter
    @OneToMany(
            mappedBy = "book",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Image> images;

    @Setter
    @Column(nullable = false)
    private Long discountRate;

    @Setter
    @Column(nullable = false)
    private Long likeCount;

    public Book(String title, String author, Long salePrice, String publisher, LocalDate publishedAt, String isbn) {
        this.title = title;
        this.author = author;
        this.salePrice = salePrice;
        this.publisher = publisher;
        this.publishedAt = publishedAt;
        this.isbn = isbn;
        this.images = new ArrayList<>();
    }


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

    public void addImages(List<Image> reviewImages) {
        images.addAll(reviewImages);
    }
}
