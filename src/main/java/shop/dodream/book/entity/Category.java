package shop.dodream.book.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id") @Getter
    private Long id;

    @Column(nullable = false) @Setter
    private String categoryName;

    @Column(nullable = false) @Setter
    private Long depth;

    @ManyToOne @Setter
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> children = new ArrayList<>();

    public void addChild(Category child) {
        if (!this.children.contains(child)) {
            this.children.add(child);
            child.setParent(this);
        }
    }

    public Category(Long id) {
        this.id = id;
    }

}
