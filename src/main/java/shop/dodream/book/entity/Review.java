package shop.dodream.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import shop.dodream.book.dto.ReviewUpdateRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity{
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    @Setter
    @Max(10)
    @Min(0)
    @Column(columnDefinition = "TINYINT")
    private byte rating;

    @Setter
    @NotBlank
    @Size(max = 200)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", updatable = false)
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

    public List<String> update(ReviewUpdateRequest request) {
        if ((request.getRating() * 2) % 1 != 0) {
            throw new IllegalArgumentException("0 ~ 5 사이, 0.5 단위의 값을 사용해야 합니다");
        }

        byte ratingScale = (byte)(request.getRating()* 2);
        this.content = request.getContent();
        this.rating = ratingScale;

        if (Objects.nonNull(request.getImages())) {
            Set<String> existImages = images.stream()
                    .map(Image::getUuid)
                    .collect(Collectors.toSet());

            Set<String> deletedImages = existImages.stream()
                    .filter(img -> !request.getImages().contains(img))
                    .collect(Collectors.toSet());

           images.removeIf(img -> deletedImages.contains(img.getUuid()));

            return new ArrayList<>(deletedImages);
        }
        return List.of();
    }


    public Review(byte rating, String content, String userID, Book book) {
        this.book = book;
        this.rating = rating;
        this.content = content;
        this.userId = userID;
        this.images = new ArrayList<>();
    }

    public void addImage(List<Image> reviewImages) {
        images.addAll(reviewImages);
    }
}