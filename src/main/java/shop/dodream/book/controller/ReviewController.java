package shop.dodream.book.controller;

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
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/books/{book-id}/reviews")
    public List<ReviewResponseRecord> getReviews(@PathVariable("book-id") Long bookId) {
        return reviewService.getReviewsByBookId(bookId);
    }

    @PostMapping("/books/{book-id}/reviews")
    public ResponseEntity<Void> creteReview(@PathVariable("book-id") Long bookId,
                                            @RequestHeader("X-USER-ID") String userId,
                                            @Valid @RequestPart("review") ReviewCreateRequest reviewCreateRequest,
                                            @ValidatedFiles @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        reviewService.createReview(bookId, userId, reviewCreateRequest, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/me/reviews")
    public List<ReviewResponseRecord> getReviews(@RequestHeader("X-USER-ID") String userId){
        return reviewService.getReviewsByUserId(userId);
    }

    @GetMapping("/users/me/reviews/{review-id}")
    public ReviewResponseRecord getReview(@PathVariable("review-id") Long reviewId,
                                          @RequestHeader("X-USER-ID") String userId){
        return reviewService.getReview(reviewId, userId);
    }

    @PutMapping("/users/me/reviews/{review-id}")
    public ResponseEntity<Void> updateReview(@PathVariable("review-id") Long reviewId,
                                             @RequestHeader("X-USER-ID") String userId,
                                             @Valid @RequestPart("review") ReviewUpdateRequest reviewUpdateRequest,
                                             @ValidatedFiles @RequestPart(value = "files", required = false) List<MultipartFile> files){
        reviewService.updateReview(reviewId, userId, reviewUpdateRequest, files);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/users/me/reviews/{review-id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("review-id") Long reviewId,
                                             @RequestHeader("X-USER-ID") String userId) {
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}