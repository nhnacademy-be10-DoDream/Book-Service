package shop.dodream.book.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.dodream.book.dto.BookDocument;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookSortType;
import shop.dodream.book.service.BookSearchService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public Page<BookItemResponse> searchBooks(String keyword, BookSortType sortType, Pageable pageable) {
        try {
            MultiMatchQuery multiMatchQuery = new MultiMatchQuery.Builder()
                    .query(keyword)
                    .fields("title^100", "author^50", "categoryNames^10", "description^5")
                    .build();

            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder()
                    .must(m -> m.multiMatch(multiMatchQuery)) // keyword 검색 조건
                    .mustNot(mn -> mn.term(t -> t.field("status").value("REMOVED"))); // status=REMOVED 제외 조건

            if (sortType == BookSortType.RATING) {
                boolQueryBuilder.filter(f -> f.range(r -> r
                        .field("reviewCount")
                        .gte(JsonData.of(100))
                ));
            }

            Query query = new Query.Builder()
                    .bool(boolQueryBuilder.build())
                    .build();

            SortOptions sortOption = sortType.toSortOption();

            SearchRequest request = new SearchRequest.Builder()
                    .index("dodream_books")
                    .query(query)
                    .from((int) pageable.getOffset())  // offset = page * size
                    .size(pageable.getPageSize())
                    .sort(sortOption != null ? Collections.singletonList(sortOption) : Collections.emptyList())
                    .build();

            SearchResponse<BookDocument> response = elasticsearchClient.search(request, BookDocument.class);

            List<BookItemResponse> content = response.hits().hits().stream()
                    .map(Hit::source)
                    .map(BookItemResponse::new)
                    .toList();

            long totalHits = response.hits().total() != null
                    ? response.hits().total().value()
                    : content.size();

            return new PageImpl<>(content, pageable, totalHits);

        } catch (IOException e) {
            e.printStackTrace();
            return Page.empty(pageable);
        }
    }
}
