package shop.dodream.book.exception;

import java.io.IOException;

public class BookSearchException extends BadRequestException {
    public BookSearchException(IOException e) {
        super("Elasticsearch 검색 중 오류가 발생했습니다." + e);
    }
}
