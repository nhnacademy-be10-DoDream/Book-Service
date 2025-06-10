package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(nullable = false)
    private String tagName;
}
