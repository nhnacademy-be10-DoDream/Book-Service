package shop.dodream.book.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("도서 서비스 API")
                        .version("v1.0")
                        .description("도서와 관련된 등록, 검색, 상세 조회 등을 제공하는 API 문서입니다."));
    }
}
