package shop.dodream.book.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
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
            // 1. 검색 쿼리 (Multi Match + 가중치)
            Query query = new Query.Builder()
                    .multiMatch(new MultiMatchQuery.Builder()
                            .query(keyword)
                            .fields("title^100", "author^50", "description^10")
                            .build())
                    .build();

            // 2. 정렬 조건 설정
            SortOptions sortOption = switch (sort) {
                case "popularity" -> new SortOptions.Builder().field(f -> f.field("viewCount").order(SortOrder.Desc)).build();
                case "recent"     -> new SortOptions.Builder().field(f -> f.field("publishedAt").order(SortOrder.Desc)).build();
                case "lowPrice"   -> new SortOptions.Builder().field(f -> f.field("salePrice").order(SortOrder.Asc)).build();
                case "highPrice"  -> new SortOptions.Builder().field(f -> f.field("salePrice").order(SortOrder.Desc)).build();
                case "rating"     -> new SortOptions.Builder().field(f -> f.field("ratingAvg").order(SortOrder.Desc)).build();
                case "review"     -> new SortOptions.Builder().field(f -> f.field("reviewCount").order(SortOrder.Desc)).build();
                default           -> null;
            };

            // 3. SearchRequest 생성
            SearchRequest.Builder requestBuilder = new SearchRequest.Builder()
                    .index("dodream_books") // 실제 인덱스 이름 사용
                    .query(query)
                    .size(20);

            if (sortOption != null) {
                requestBuilder.sort(sortOption);
            }

            SearchResponse<BookDocument> response = elasticsearchClient.search(
                    requestBuilder.build(),
                    BookDocument.class
            );

            // 4. 결과 매핑
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
