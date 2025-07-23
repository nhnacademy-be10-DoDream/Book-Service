package shop.dodream.book.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
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
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.BookDocument;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookItemWithCountResponse;
import shop.dodream.book.dto.BookSortType;
import shop.dodream.book.service.BookSearchService;
import shop.dodream.book.service.CategoryService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {

    private final ElasticsearchClient elasticsearchClient;
    private final CategoryService categoryService;

    @Override
    public BookItemWithCountResponse searchBooks(String keyword, BookSortType sortType, Pageable pageable,
                                                 List<Long> categoryIds, Integer minPrice, Integer maxPrice) {
        try {
            MultiMatchQuery multiMatchQuery = new MultiMatchQuery.Builder()
                    .query(keyword)
                    .fields("title^100", "author^50", "categoryNames^10", "description^5")
                    .build();

            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder()
                    .must(m -> m.multiMatch(multiMatchQuery)) // keyword 검색 조건
                    .mustNot(mn -> mn.term(t -> t.field("status").value("REMOVED"))); // status=REMOVED 제외 조건

            if (categoryIds != null && !categoryIds.isEmpty()) {
                boolQueryBuilder.filter(f -> f.terms(t -> t
                        .field("categoryIds")
                        .terms(terms -> terms.value(categoryIds.stream()
                                .map(FieldValue::of)
                                .toList()))
                ));
            }

            if (minPrice != null || maxPrice != null) {
                boolQueryBuilder.filter(f -> f.range(r -> {
                    r.field("salePrice");
                    if (minPrice != null) r.gte(JsonData.of(minPrice));
                    if (maxPrice != null) r.lte(JsonData.of(maxPrice));
                    return r;
                }));
            }

            if (sortType == BookSortType.RATING) {
                boolQueryBuilder.filter(f -> f.range(r -> r
                        .field("reviewCount")
                        .gte(JsonData.of(10))
                ));
            }

            Query query = new Query.Builder()
                    .bool(boolQueryBuilder.build())
                    .build();

            SortOptions sortOption = sortType.toSortOption();

            SearchRequest request = new SearchRequest.Builder()
                    .index("dodream_books_v2")
                    .query(query)
                    .from((int) pageable.getOffset())  // offset = page * size
                    .size(pageable.getPageSize())
                    .sort(sortOption != null ? Collections.singletonList(sortOption) : Collections.emptyList())
                    .aggregations("category_count", agg -> agg
                            .terms(t -> t
                                    .field("categoryIds")
                                    .size(1000)
                            ))
                    .build();

            SearchResponse<BookDocument> response = elasticsearchClient.search(request, BookDocument.class);

            List<BookItemResponse> content = response.hits().hits().stream()
                    .map(Hit::source)
                    .map(BookItemResponse::new)
                    .toList();

            long totalHits = response.hits().total() != null
                    ? response.hits().total().value()
                    : content.size();

            Map<Long, Long> categoryCountMap = new HashMap<>();
            var buckets = response.aggregations()
                    .get("category_count")
                    .lterms()
                    .buckets()
                    .array();

            for (var bucket : buckets) {
                Long categoryId = bucket.key();
                long count = bucket.docCount();
                categoryCountMap.put(categoryId, count);
            }

            List<CategoryResponse> depth1Categories = categoryService.getCategoriesDepth(1L);
            List<CategoryResponse> allCategories = categoryService.getCategories();

            Map<Long, List<CategoryResponse>> parentToChildrenMap = allCategories.stream()
                    .filter(c -> c.getParentId() != null)
                    .collect(Collectors.groupingBy(CategoryResponse::getParentId));

            for (CategoryResponse depth1 : depth1Categories) {
                long sum = 0L;
                List<CategoryResponse> secondLevelCategories = parentToChildrenMap.getOrDefault(depth1.getCategoryId(), List.of());
                for (CategoryResponse second : secondLevelCategories) {
                    sum += categoryCountMap.getOrDefault(second.getCategoryId(), 0L);
                }
                if (sum > 0) {
                    categoryCountMap.put(depth1.getCategoryId(), sum);
                }
            }

            Page<BookItemResponse> page = new PageImpl<>(content, pageable, totalHits);
            return new BookItemWithCountResponse(page, categoryCountMap);

        } catch (IOException e) {
            e.printStackTrace();
            return new BookItemWithCountResponse(Page.empty(), Collections.emptyMap());
        }
    }
}
