package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.dodream.book.infra.dto.AladdinBookResponse;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
public class AladdinBookSearchResult {

    private int totalResults;
    private List<BookItemResponse> items;
}
