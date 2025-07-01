package shop.dodream.book.core.event;

import java.util.List;

public record ReviewImageDeleteEvent(List<String> deleteKeys) {
}
