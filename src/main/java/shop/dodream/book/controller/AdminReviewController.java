package shop.dodream.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.annotation.ValidatedReviewFiles;
import shop.dodream.book.dto.ReviewUpdateRequest;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin Review", description = "관리자 리뷰 관리 API")
public class AdminReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 전체 조회", description = "관리자가 모든 리뷰를 조회합니다.")
    @GetMapping("/reviews")
    public List<ReviewResponseRecord> getReviews(){
        return reviewService.getReviews();
    }

    @Operation(summary = "사용자 리뷰 조회", description = "특정 사용자의 리뷰를 조회합니다.")
    @GetMapping("/users/{user-id}/reviews")
    public List<ReviewResponseRecord> getReviewsByUserId(@PathVariable("user-id") String userId){
        return reviewService.getReviewsByUserId(userId);
    }

    @Operation(summary = "리뷰 상세 조회", description = "리뷰 ID를 기준으로 리뷰 상세 정보를 조회합니다.")
    @GetMapping("/reviews/{review-id}")
    public ReviewResponseRecord getReview(@PathVariable("review-id") Long reviewId){
        return reviewService.getReview(reviewId);
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 내용, 파일을 수정할 수 있습니다.")
    @PutMapping("/reviews/{review-id}")
    public ResponseEntity<Void> updateReview(@PathVariable("review-id") Long reviewId,
                                             @Valid @RequestPart("review") ReviewUpdateRequest reviewUpdateRequest,
                                             @ValidatedReviewFiles @RequestPart(value = "files", required = false) List<MultipartFile> files){
        reviewService.updateReview(reviewId, reviewUpdateRequest, files);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 ID를 기준으로 리뷰를 삭제합니다.")
    @DeleteMapping("/reviews/{review-id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("review-id") Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
