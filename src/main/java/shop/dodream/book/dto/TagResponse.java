package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.dodream.book.entity.Tag;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TagResponse {
    private Long tagId;
    private String tagName;

    public TagResponse(Tag tag) {
        this.tagId = tag.getId();
        this.tagName = tag.getTagName();
    }
}
