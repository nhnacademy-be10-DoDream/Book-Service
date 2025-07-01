package shop.dodream.book.core.event;

import java.util.List;

public record BookImageDeleteEvent(List<String> deleteKeys) {
}
