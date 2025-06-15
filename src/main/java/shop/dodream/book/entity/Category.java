package shop.dodream.book.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false) @Setter
    private String categoryName;

    @Column(nullable = false) @Setter
    private Long depth;

    @ManyToOne @Setter
    @JoinColumn(name = "parent_id")
    private Category category;


}
