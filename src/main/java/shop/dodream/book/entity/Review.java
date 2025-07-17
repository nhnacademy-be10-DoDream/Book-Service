package shop.dodream.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import shop.dodream.book.dto.ReviewUpdateRequest;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_book_id", columnList = "bookId"),
        @Index(name = "idx_user_id", columnList = "userId")
})
public class Review extends BaseTimeEntity{
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @Setter
    @Getter
    @Max(10)
    @Min(0)
    @Column(columnDefinition = "TINYINT")
    private Short rating;

    @Setter
    @NotBlank
    @Size(max = 200)
    private String content;

    @NotNull
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", updatable = false)
    private Book book;

    @Getter
    @OneToMany(
            mappedBy = "review",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<Image> images;

    @NotNull
    @Column(updatable = false)
    private String userId;

    @Column(updatable = false)
    private long orderItemId;

    public List<String> update(ReviewUpdateRequest request) {

        this.content = request.getContent();
        this.rating = request.getRating();

        if (Objects.nonNull(request.getImages())) {
            Set<String> requestImages = new HashSet<>(request.getImages());

            Set<String> deletedImages = images.stream()
                    .map(Image::getUuid).collect(Collectors.toSet());
            deletedImages.removeAll(requestImages);

            images.removeIf(img -> deletedImages.contains(img.getUuid()));

            return new ArrayList<>(deletedImages);
        }

        List<String> allImages = images.stream()
                .map(Image::getUuid)
                .toList();

        images.clear();

        return allImages;
    }


    public Review(Short rating, String content, String userID, long orderItemId, Book book) {
        this.book = book;
        this.rating = rating;
        this.content = content;
        this.userId = userID;
        this.orderItemId = orderItemId;
        this.images = new ArrayList<>();
    }

    public void addImages(List<Image> reviewImages) {
        images.addAll(reviewImages);
    }
}