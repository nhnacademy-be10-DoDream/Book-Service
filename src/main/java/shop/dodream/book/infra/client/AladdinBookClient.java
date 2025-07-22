package shop.dodream.book.infra.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.dodream.book.infra.dto.AladdinBookResponse;

@FeignClient(name = "aladdinBookClient", url = "${aladdin.book.base-url}")
public interface AladdinBookClient {


    @GetMapping(value = "/ItemSearch.aspx")
    AladdinBookResponse searchBook(
            @RequestParam("ttbkey") String ttbkey,
            @RequestParam("Query") String query,
            @RequestParam("MaxResults") String maxResults,
            @RequestParam("start") String start,
            @RequestParam("output") String output,
            @RequestParam("Version") String version,
            @RequestParam("Cover") String cover
    );

}
