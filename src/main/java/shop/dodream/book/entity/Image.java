package shop.dodream.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", updatable = false)
    private Book book;

    private boolean isThumbnail;

    public Image(Review review, String uuid) {
        this.review = review;
        this.uuid = uuid;
    }

    public Image(Book book, String uuid) {
        this.book = book;
        this.uuid = uuid;
    }

    public Image(Book book, String uuid, boolean isThumbnail) {
        this.book = book;
        this.uuid = uuid;
        this.isThumbnail = isThumbnail;
    }
}