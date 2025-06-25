package shop.dodream.book.core.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "naver.book")
public class NaverBookProperties {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
}
