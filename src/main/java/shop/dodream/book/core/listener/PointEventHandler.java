package shop.dodream.book.core.listener;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import shop.dodream.book.core.event.PointEarnEvent;
import shop.dodream.book.infra.client.UserClient;
import shop.dodream.book.infra.dto.PointRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointEventHandler {
    private final UserClient userClient;

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            retryFor = {FeignException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handlePointEarnEvent(PointEarnEvent event) {
        PointRequest pointRequest = new PointRequest(event.amount(), "EARN", event.policyType(), true, null);
        try {
            userClient.pointEarn(event.userId(), pointRequest);
            log.info("포인트 적립 요청 성공 - userId: {}, policyType: {}", event.userId(), event.policyType());
        } catch (Exception e) {
            log.error("포인트 적립 요청 실패 - userId: {}, policyType: {}", event.userId(), event.policyType(), e);
        }
    }

}