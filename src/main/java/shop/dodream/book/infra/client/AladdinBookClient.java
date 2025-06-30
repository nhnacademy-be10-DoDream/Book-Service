package shop.dodream.book.infra.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.dodream.book.infra.dto.AladdinBookResponse;

@FeignClient(name = "aladdinBookClient", url = "${aladdin.book.base-url}")
public interface AladdinBookClient {


    @GetMapping(value = "/ItemLookUp.aspx?", consumes = MediaType.APPLICATION_JSON_VALUE)
    AladdinBookResponse searchBook(
            @RequestParam("ttbkey") String ttbkey,
            @RequestParam("itemIdType") String isbn,
            @RequestParam("ItemId") String itemId,
            @RequestParam("output") String output,
            @RequestParam("Version") String version
    );

}
