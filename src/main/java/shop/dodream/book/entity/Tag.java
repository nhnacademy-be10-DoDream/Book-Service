package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    @Getter

    private Long id;

    @Column(name = "tag_name", nullable = false)
    @Setter
    @Getter
    private String tagName;
}
