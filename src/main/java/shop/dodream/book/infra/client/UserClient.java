package shop.dodream.book.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import shop.dodream.book.infra.dto.PointRequest;

@FeignClient(name = "user")
public interface UserClient {

    @GetMapping("/users/{user-id}/purchases")
    void pointEarn(@PathVariable("user-id") String userId,
                   @RequestBody PointRequest request);

}
