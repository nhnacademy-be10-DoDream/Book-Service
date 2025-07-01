package shop.dodream.book.core.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import shop.dodream.book.core.event.BookImageDeleteEvent;
import shop.dodream.book.core.event.ReviewImageDeleteEvent;
import shop.dodream.book.service.FileService;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageEventHandler {
    private final FileService fileService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            retryFor = {RuntimeException.class, IOException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handleBookImageDeleted(BookImageDeleteEvent event) {
        try {
            fileService.deleteBookImage(event.deleteKeys());
            log.info("파일 삭제 완료 - 기존 파일 삭제: {} 개", event.deleteKeys().size());
        } catch (Exception e) {
            log.error("파일 삭제 실패 - keys: {}", event.deleteKeys(), e);
        }
    }



    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            retryFor = {RuntimeException.class, IOException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handleReviewImageDelete(ReviewImageDeleteEvent event) {
        try {
            fileService.deleteReviewImage(event.deleteKeys());
            log.info("파일 삭제 완료 - 기존 파일 삭제: {} 개", event.deleteKeys().size());
        } catch (Exception e) {
            log.error("파일 삭제 실패 - keys: {}", event.deleteKeys(), e);
        }
    }


}