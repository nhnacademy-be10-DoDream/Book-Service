package shop.dodream.book.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dodream.book.dto.BookDocument;
import shop.dodream.book.service.BookDocumentUpdater;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookDocumentUpdaterImpl implements BookDocumentUpdater {

    private final ElasticsearchClient client;

    @Override
    public void increaseReviewStatus(Long bookId, float newRating) {
        try {
            client.update(u -> u
                            .index("dodream_books")
                            .id(bookId.toString())
                            .script(s -> s
                                    .inline(i -> i
                                            .source("""
                        ctx._source.reviewCount += 1;
                        ctx._source.ratingAvg = 
                          ((ctx._source.ratingAvg * (ctx._source.reviewCount - 1)) + params.newRating) 
                          / ctx._source.reviewCount;
                    """)
                                            .params(Map.of(
                                                    "newRating", JsonData.of(newRating)
                                            ))
                                    )
                            ),
                    BookDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException("리뷰 통계 갱신 실패", e);
        }
    }


    // TODO 리뷰 삭제 및 수정시 es 갱신해야함
//    @Override
//    public void decreaseReviewStatus(Long bookId, float deletedRating) {
//        try {
//            client.update(u -> u
//                            .index("dodream_books")
//                            .id(bookId.toString())
//                            .script(s -> s
//                                    .inline(i -> i
//                                            .source("""
//                                    ctx._source.reviewCount -= 1;
//                                    if (ctx._source.reviewCount > 0) {
//                                        ctx._source.ratingAvg =
//                                          ((ctx._source.ratingAvg * (ctx._source.reviewCount + 1)) - params.deletedRating)
//                                          / ctx._source.reviewCount;
//                                    } else {
//                                        ctx._source.ratingAvg = 0.0;
//                                    }
//                                """)
//                                            .params(Map.of(
//                                                    "deletedRating", JsonData.of(deletedRating)
//                                            ))
//                                    )
//                            ),
//                    BookDocument.class
//            );
//        } catch (IOException e) {
//            throw new RuntimeException("리뷰 삭제 통계 갱신 실패", e);
//        }
//    }


    @Override
    public void updateBookFields(Long bookId, Map<String, Object> fieldsToUpdate) {
        try {
            client.update(u -> u
                            .index("dodream_books")
                            .id(bookId.toString())
                            .doc(fieldsToUpdate),
                    BookDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException("도서 필드 업데이트 실패", e);
        }
    }
}
