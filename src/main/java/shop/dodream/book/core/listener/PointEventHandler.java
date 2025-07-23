package shop.dodream.book.core.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import shop.dodream.book.core.event.PointEarnEvent;
import shop.dodream.book.infra.dto.PointRequest;
import shop.dodream.book.service.ProducerService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointEventHandler {
    private final ProducerService<PointRequest> producerService;

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            retryFor = {AmqpException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handlePointEarnEvent(PointEarnEvent event) {
        PointRequest request = new PointRequest(event.policyType());

        producerService.sendMessage(event.userId(), request);

        log.info("포인트 적립 요청 성공 - userId: {}, policyType: {}", event.userId(), event.policyType());
    }

    @Recover
    public void recover(AmqpException ex, PointEarnEvent event) {
        log.error("포인트 적립 요청 최종 실패 - userId: {}, policyType: {}, 재시도 횟수 초과", event.userId(), event.policyType(), ex);
    }

}