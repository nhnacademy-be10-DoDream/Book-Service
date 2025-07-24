package shop.dodream.book.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import shop.dodream.book.core.properties.PointRoutingProperties;
import shop.dodream.book.infra.dto.PointRequest;
import shop.dodream.book.service.impl.RabbitPointProducerServiceImpl;

@ExtendWith(MockitoExtension.class)
class RabbitPointProducerServiceImplTest {
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private PointRoutingProperties properties;
    @InjectMocks
    private RabbitPointProducerServiceImpl producerService;

    @Test
    void sendMessage() {
        String userId = "testUser";
        PointRequest request = Mockito.mock(PointRequest.class);
        String exchange = "point-exchange";
        String routingKey = "point.earn";

        Mockito.when(properties.getExchange()).thenReturn(exchange);
        Mockito.when(properties.getEarnKey()).thenReturn(routingKey);

        Assertions.assertDoesNotThrow(()->producerService.sendMessage(userId, request));

    }
}
