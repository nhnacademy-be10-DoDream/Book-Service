package shop.dodream.book.service;

import java.util.Map;

public interface BookDocumentUpdater {
//    void updateReviewStats(Long bookId, long viewCount, float ratingAvg);
//    void updateCategoryName(Long bookId, String categoryName);

    void updateFields(Long bookId, Map<String, Object> fieldMap);
}
