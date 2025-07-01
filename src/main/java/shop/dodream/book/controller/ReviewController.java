package shop.dodream.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.annotation.ValidatedFiles;
import shop.dodream.book.dto.ReviewCreateRequest;
import shop.dodream.book.dto.ReviewUpdateRequest;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 API (사용자)")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "도서 리뷰 목록 조회", description = "특정 도서의 리뷰 목록을 조회합니다.")
    @GetMapping("/books/{book-id}/reviews")
    public List<ReviewResponseRecord> getReviews(@PathVariable("book-id") Long bookId) {
        return reviewService.getReviewsByBookId(bookId);
    }

    @Operation(summary = "도서 리뷰 작성", description = "도서에 대한 리뷰를 작성합니다. (파일 첨부 가능)")
    @PostMapping("/books/{book-id}/reviews")
    public ResponseEntity<Void> creteReview(@PathVariable("book-id") Long bookId,
                                            @RequestHeader("X-USER-ID") String userId,
                                            @Valid @RequestPart("review") ReviewCreateRequest reviewCreateRequest,
                                            @ValidatedFiles @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        reviewService.createReview(bookId, userId, reviewCreateRequest, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "내 리뷰 목록 조회", description = "사용자가 작성한 리뷰 목록을 조회합니다.")
    @GetMapping("/users/me/reviews")
    public List<ReviewResponseRecord> getReviews(@RequestHeader("X-USER-ID") String userId){
        return reviewService.getReviewsByUserId(userId);
    }

    @Operation(summary = "내 리뷰 상세 조회", description = "사용자가 작성한 특정 리뷰를 조회합니다.")
    @GetMapping("/users/me/reviews/{review-id}")
    public ReviewResponseRecord getReview(@PathVariable("review-id") Long reviewId,
                                          @RequestHeader("X-USER-ID") String userId){
        return reviewService.getReview(reviewId, userId);
    }

    @Operation(summary = "내 리뷰 수정", description = "사용자가 작성한 리뷰를 수정합니다. (파일 첨부 가능)")
    @PutMapping("/users/me/reviews/{review-id}")
    public ResponseEntity<Void> updateReview(@PathVariable("review-id") Long reviewId,
                                             @RequestHeader("X-USER-ID") String userId,
                                             @Valid @RequestPart("review") ReviewUpdateRequest reviewUpdateRequest,
                                             @ValidatedFiles @RequestPart(value = "files", required = false) List<MultipartFile> files){
        reviewService.updateReview(reviewId, userId, reviewUpdateRequest, files);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "내 리뷰 삭제", description = "사용자가 작성한 리뷰를 삭제합니다.")
    @DeleteMapping("/users/me/reviews/{review-id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("review-id") Long reviewId,
                                             @RequestHeader("X-USER-ID") String userId) {
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}