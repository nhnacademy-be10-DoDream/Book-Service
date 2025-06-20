package shop.dodream.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookWithCategoriesResponse {
    private Long bookId;
    private List<CategoryResponse> categories;
}
