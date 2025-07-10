package shop.dodream.book.core.event;

public record PointEarnEvent(String userId, long amount, String policyType) {
}
