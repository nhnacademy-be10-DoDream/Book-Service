package shop.dodream.book.command;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class ReviewUpdateCommand {
    private Byte rating;
    private String content;
    private Set<String> images;
}