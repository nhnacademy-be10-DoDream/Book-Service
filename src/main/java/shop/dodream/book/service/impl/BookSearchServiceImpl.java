package shop.dodream.book.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dodream.book.dto.BookDocument;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.service.BookSearchService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public List<BookItemResponse> searchBooks(String keyword, String sort) {
        try {
            Query query;

            MultiMatchQuery multiMatchQuery = new MultiMatchQuery.Builder()
                    .query(keyword)
                    .fields("title^100", "author^50", "description^10")
                    .build();

            if ("rating".equals(sort)) {
                query = new Query.Builder()
                        .bool(b -> b
                                .must(m -> m.multiMatch(multiMatchQuery))
                                .filter(f -> f.range(r -> r
                                        .field("reviewCount")
                                        .gte(JsonData.of(100))
                                ))
                        )
                        .build();
            } else {
                query = new Query.Builder()
                        .multiMatch(multiMatchQuery)
                        .build();
            }

            SortOptions sortOption = switch (sort) {
                case "popularity" -> new SortOptions.Builder().field(f -> f.field("viewCount").order(SortOrder.Desc)).build();
                case "recent"     -> new SortOptions.Builder().field(f -> f.field("publishedAt").order(SortOrder.Desc)).build();
                case "lowPrice"   -> new SortOptions.Builder().field(f -> f.field("salePrice").order(SortOrder.Asc)).build();
                case "highPrice"  -> new SortOptions.Builder().field(f -> f.field("salePrice").order(SortOrder.Desc)).build();
                case "rating"     -> new SortOptions.Builder().field(f -> f.field("ratingAvg").order(SortOrder.Desc)).build();
                case "review"     -> new SortOptions.Builder().field(f -> f.field("reviewCount").order(SortOrder.Desc)).build();
                default           -> null;
            };

            SearchRequest request = new SearchRequest.Builder()
                    .index("dodream_books")
                    .query(query)
                    .size(20)
                    .sort(sortOption != null ? Collections.singletonList(sortOption) : Collections.emptyList())
                    .build();

            SearchResponse<BookDocument> response = elasticsearchClient.search(
                    request,
                    BookDocument.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .map(BookItemResponse::new)
                    .toList();

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
