package shop.dodream.book.service;


import java.util.Map;

public interface BookDocumentUpdater {

    void increaseReviewStatus(Long bookId, float newRating);

    void decreaseReviewStatus(Long bookId, float newRating);

    void updateBookFields(Long bookId, Map<String, Object> fieldsToUpdate);

    void updateReviewStatus(Long bookId, float oldRating, float newRating);

}
