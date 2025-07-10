package shop.dodream.book.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.dodream.book.dto.PurchaseCheckResponse;

import java.util.List;

@FeignClient(name = "order")
public interface OrderClient {

    @GetMapping("/orders/users/{user-id}/books/{book-id}/items")
    List<PurchaseCheckResponse> checkOrderItem(@PathVariable("user-id") String userId,
                                               @PathVariable("book-id") Long bookId);

}
