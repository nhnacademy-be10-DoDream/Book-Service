package shop.dodream.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;

    @Getter
    @NotBlank
    @Size(max = 200)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId", updatable = false)
    private Review review;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", updatable = false)
    private Book book;

    private boolean isThumbnail;

    public Image(Review review, String uuid) {
        this.review = review;
        this.uuid = uuid;
    }


    public Image(Book book, String uuid, boolean isThumbnail) {
        this.book = book;
        this.uuid = uuid;
        this.isThumbnail = isThumbnail;
    }
}