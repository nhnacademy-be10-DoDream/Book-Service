package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.dto.BookUpdateRequest;

import java.time.LocalDate;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(
        name = "book",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_isbn", columnNames = "isbn")
        },
        indexes = {
                @Index(name = "idx_sale_price", columnList = "sale_price"),
                @Index(name = "idx_created_at", columnList = "created_at"),
                @Index(name = "idx_view_count", columnList = "view_count")
        }
)
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

    @Column(nullable = false)
    private LocalDate publishedAt;

    @Column(nullable = false)
    private String isbn;

    @Setter
    @Column(nullable = false)
    private Long regularPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @Setter
    @Column(nullable = false)
    private Long salePrice;

    @Setter
    @Column(nullable = false)
    private Boolean isGiftable;


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
    private List<Image> images = new ArrayList<>();

//    @Setter
    @Column(nullable = false)
    private Long discountRate;



    // 책 직접등록용
    public Book(String title, String description, String author, String publisher, LocalDate publishedAt, String isbn, Long regularPrice, BookStatus status, Long salePrice, Boolean isGiftable, Long viewCount, Long bookCount) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.publishedAt = publishedAt;
        this.isbn = isbn;
        this.regularPrice = regularPrice;
        this.status = status;
        this.salePrice = salePrice;
        this.isGiftable = isGiftable;
        this.viewCount = viewCount;
        this.bookCount = bookCount;
        this.discountRate = calculateDiscountRate(regularPrice,salePrice);
        this.images = new ArrayList<>();
    }

    // 알라딘용
    public Book(String title, String description, String author, String publisher, LocalDate publishedAt, String isbn, Long regularPrice, BookStatus status, Long salePrice, Boolean isGiftable, Long viewCount, Long bookCount, Long discountRate) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.publishedAt = publishedAt;
        this.isbn = isbn;
        this.regularPrice = regularPrice;
        this.status = status;
        this.salePrice = salePrice;
        this.isGiftable = isGiftable;
        this.viewCount = viewCount;
        this.bookCount = bookCount;
        this.discountRate = discountRate;
        this.images = new ArrayList<>();
    }



    public void updateTextFields(BookUpdateRequest bookUpdateRequest) {

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


        this.discountRate = calculateDiscountRate(regularPrice, salePrice);



    }

    // TODO 리스트 이미지 말고 단일로 이미지로 처리
    public void addImages(List<Image> imageList) {
        for (Image image: imageList){
            images.add(image);
            image.setBook(this);
        }
    }

    private Long calculateDiscountRate(Long regularPrice, Long salePrice) {
        return Math.round((1 - (double) salePrice / regularPrice) * 100);
    }

    public void updateStatusByBookCount() {
        if (this.status != BookStatus.REMOVED) {
            if (this.bookCount == 0) {
                this.status = BookStatus.SOLD_OUT;
            } else if (this.bookCount <= 5) {
                this.status = BookStatus.LOW_STOCK;
            } else {
                this.status = BookStatus.SELL;
            }
        }
    }

    public void markAsRemoved(){
        this.status = BookStatus.REMOVED;
    }




}
