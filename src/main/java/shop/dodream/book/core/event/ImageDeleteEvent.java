package shop.dodream.book.core.event;

import java.util.List;

public record ImageDeleteEvent(List<String> deleteKeys) {
}
