package shop.dodream.book.dto;

import lombok.Getter;

import java.util.List;


@Getter
public class IsbnListRequest {
    List<String> isbnList;
}
