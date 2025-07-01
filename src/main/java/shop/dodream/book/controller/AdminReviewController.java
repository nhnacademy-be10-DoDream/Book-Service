package shop.dodream.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.annotation.ValidatedFiles;
import shop.dodream.book.dto.ReviewUpdateRequest;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminReviewController {
    private final ReviewService reviewService;

    @GetMapping("/reviews")
    public List<ReviewResponseRecord> getReviews(){
        return reviewService.getReviews();
    }

    @GetMapping("/users/{user-id}/reviews")
    public List<ReviewResponseRecord> getReviewsByUserId(@PathVariable("user-id") String userId){
        return reviewService.getReviewsByUserId(userId);
    }

    @GetMapping("/reviews/{review-id}")
    public ReviewResponseRecord getReview(@PathVariable("review-id") Long reviewId) {
        return reviewService.getReview(reviewId);
    }

    @PutMapping("/reviews/{review-id}")
    public ResponseEntity<Void> updateReview(@PathVariable("review-id") Long reviewId,
                                             @Valid @RequestPart("review") ReviewUpdateRequest reviewUpdateRequest,
                                             @ValidatedFiles @RequestPart(value = "files", required = false) List<MultipartFile> files){
        reviewService.updateReview(reviewId, reviewUpdateRequest, files);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/reviews/{review-id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("review-id") Long reviewId){
        reviewService.deleteReview(reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
