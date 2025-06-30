package shop.dodream.book.core.event;

import java.util.List;

public record BookImageDeleteEvent(Long bookId, List<String> deleteKeys) {
}
