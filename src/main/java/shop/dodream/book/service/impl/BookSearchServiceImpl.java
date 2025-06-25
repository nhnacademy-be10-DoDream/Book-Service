package shop.dodream.book.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.dodream.book.service.BookSearchService;


import java.util.Map;



@Service
public class BookSearchServiceImpl implements BookSearchService {
    private final RestTemplate restTemplate;
    private final String elasticsearchUrl = "http://s4.java21.net:9200/dream_books/_search";

    public BookSearchServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication("elastic", "nhnacademy123!")
                .build();
    }

    public Map<String, Object> searchBooks(String keyword) {
        String queryJson = """
        {
          "query": {
            "multi_match": {
              "query": "%s",
              "fields": [
                "title^100",
                "title_synonym^50",
                "title_jaso^20",
                "description^10",
                "author^30"
              ]
            }
          }
        }
        """.formatted(keyword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(queryJson, headers);

        ResponseEntity<Map> response = restTemplate.exchange(elasticsearchUrl, HttpMethod.POST, request, Map.class);

        return response.getBody();
    }
}
