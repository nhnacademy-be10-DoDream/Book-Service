package shop.dodream.book.core.event;

import java.util.List;

public record ReviewImageDeleteEvent(Long reviewId, List<String> deleteKeys) {
}
