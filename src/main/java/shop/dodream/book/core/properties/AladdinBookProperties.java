package shop.dodream.book.core.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "aladdin.book")
public class AladdinBookProperties {
    private String baseUrl;
    private String ttbkey;
    private String itemIdType;
    private String output;
    private String version;
    private String cover;
}
