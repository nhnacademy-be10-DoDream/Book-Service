package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shop.dodream.book.core.config.RabbitConfig;
import shop.dodream.book.core.properties.PointRoutingProperties;
import shop.dodream.book.infra.dto.PointRequest;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(RabbitConfig.class)
public class RabbitPointProducerServiceImpl {
    private final RabbitTemplate rabbitTemplate;
    private final PointRoutingProperties properties;


    @Scheduled(fixedRate = 3000)
    public void sendMessage() {
        PointRequest request = new PointRequest(
                "REVIEW"
        );

        String exchange = properties.getExchange();
        String routingKey = properties.getEarnKey();

        rabbitTemplate.convertAndSend(exchange, routingKey, request, message -> {
            message.getMessageProperties().setHeader("X-USER-ID", "user");
            return message;
        });
        log.info("Message Complete (Exchange: {}, RoutingKey: {}, Message: {})", exchange, routingKey, request);
    }
}
