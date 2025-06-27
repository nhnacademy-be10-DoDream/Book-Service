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
    private Short rating;

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

        this.content = request.getContent();
        this.rating = request.getRating();

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


    public Review(Short rating, String content, String userID, Book book) {
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