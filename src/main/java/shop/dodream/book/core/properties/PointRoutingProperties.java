package shop.dodream.book.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.rabbitmq.point")
public class PointRoutingProperties {
    private String exchange;
    private String earnKey;
}