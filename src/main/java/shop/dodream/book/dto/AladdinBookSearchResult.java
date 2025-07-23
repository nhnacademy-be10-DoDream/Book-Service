package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AladdinBookSearchResult {

    private int totalResults;
    private List<BookItemResponse> items;
}
