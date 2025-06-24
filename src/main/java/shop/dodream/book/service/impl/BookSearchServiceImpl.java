package shop.dodream.book.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import shop.dodream.book.dto.BookDocument;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookSearchResponse;
import shop.dodream.book.exception.BookSearchException;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.service.BookSearchService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {
    private final BookRepository bookRepository;
    private final ElasticsearchClient esClient;
    private final ElasticsearchOperations elasticsearchOperations;

//    public void indexAllBooks() {
//        List<Book> books = bookRepository.findAll();
//
//        for (Book book : books) {
//            try {
//                BookRegisterResponse doc = new BookRegisterResponse(book);
//
//                var response = esClient.index(i -> i
//                        .index("books")
//                        .id(book.getId().toString())
//                        .document(doc)
//                );
//            } catch (IOException e) {
//                System.err.println("인덱싱 실패: " + book.getTitle());
//            }
//        }
//    }

    public BookSearchResponse searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new BookSearchResponse(0, Collections.emptyList());
        }

        try {
            SearchResponse<BookDocument> response = esClient.search(s -> s
                            .index("dodream_books")
                            .query(q -> {
                                BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

                                boolQueryBuilder.should(sh -> sh
                                        .multiMatch(mm -> mm
                                                .fields("title.nori^5", "description^1", "author^2", "publisher^1.5")
                                                .query(keyword)
                                                .analyzer("korean_with_icu")
                                        )
                                );

                                String chosungQuery = keyword.replace(" ", "");
                                if (!chosungQuery.isEmpty()) {
                                    boolQueryBuilder.should(sh -> sh
                                            .prefix(p -> p
                                                    .field("title.jaso")
                                                    .value(chosungQuery)
                                                    .boost(3.0f)
                                            )
                                    );
                                }

                                boolQueryBuilder.should(sh -> sh
                                        .match(m -> m
                                                .field("title.synonym")
                                                .query(keyword)
                                                .analyzer("synonym_analyzer")
                                                .boost(4.0f)
                                        )
                                );

                                boolQueryBuilder.minimumShouldMatch("1");

                                return q.bool(boolQueryBuilder.build());
                            })

                            .sort(so -> so.score(score -> score.order(SortOrder.Desc)))
                    // .size(10) // 페이지당 개수
                    // .from(0)  // 시작 위치
                    , BookDocument.class);

            List<BookItemResponse> bookItems = response.hits().hits().stream()
                    .map(hit -> new BookItemResponse(Objects.requireNonNull(hit.source())))
                    .collect(Collectors.toList());

            return new BookSearchResponse(response.hits().total().value(), bookItems);

        } catch (IOException e) {
            throw new BookSearchException(e);
        }
    }
}
