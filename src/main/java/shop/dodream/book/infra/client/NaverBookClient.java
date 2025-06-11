package shop.dodream.book.infra.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import shop.dodream.book.infra.dto.NaverBookResponse;

@FeignClient(name = "naverBookClient", url = "${naver.book.base-url}")
public interface NaverBookClient {


    @GetMapping(value = "/book.json", consumes = MediaType.APPLICATION_JSON_VALUE)
    NaverBookResponse searchBook(
            @RequestHeader("X-Naver-Client-Id") String clientId,
            @RequestHeader("X-Naver-Client-Secret") String clientSecret,
            @RequestParam("query") String query
    );

}
