package shop.dodream.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import shop.dodream.book.command.ReviewUpdateCommand;

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

    public List<String> update(ReviewUpdateCommand command) {
        this.content = command.getContent();
        this.rating = command.getRating();

        if (Objects.nonNull(command.getImages())) {
            Set<String> existImages = images.stream()
                    .map(Image::getUuid)
                    .collect(Collectors.toSet());

            Set<String> deletedImages = existImages.stream()
                    .filter(img -> !command.getImages().contains(img))
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

    public void addReviewImage(List<Image> reviewImages) {
        images.addAll(reviewImages);
    }
}