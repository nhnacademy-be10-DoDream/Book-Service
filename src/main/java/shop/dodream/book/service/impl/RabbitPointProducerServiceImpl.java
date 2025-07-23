package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import shop.dodream.book.core.config.RabbitConfig;
import shop.dodream.book.core.properties.PointRoutingProperties;
import shop.dodream.book.infra.dto.PointRequest;
import shop.dodream.book.service.ProducerService;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(RabbitConfig.class)
public class RabbitPointProducerServiceImpl implements ProducerService<PointRequest> {
    private final RabbitTemplate rabbitTemplate;
    private final PointRoutingProperties properties;

    @Override
    public void sendMessage(String userId, PointRequest request) {
        String exchange = properties.getExchange();
        String routingKey = properties.getEarnKey();

        rabbitTemplate.convertAndSend(exchange, routingKey, request, message -> {
            message.getMessageProperties().setHeader("X-USER-ID", userId);
            return message;
        });

    }
}
