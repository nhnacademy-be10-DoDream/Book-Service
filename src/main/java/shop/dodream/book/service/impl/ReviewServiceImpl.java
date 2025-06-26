package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.dto.ReviewCreateRequest;
import shop.dodream.book.dto.ReviewUpdateRequest;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.Image;
import shop.dodream.book.entity.Review;
import shop.dodream.book.exception.BookNotFoundException;
import shop.dodream.book.exception.ReviewNotFoundException;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.ReviewRepository;
import shop.dodream.book.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final FileServiceImpl fileService;

    @Transactional
    public void createReview(Long bookId, String userId, ReviewCreateRequest reviewCreateRequest, List<MultipartFile> files) {

        // 주문 내역 확인 로직 필요
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        Book book = bookRepository.getReferenceById(bookId);

        Review review = reviewCreateRequest.toEntity(book, userId);

        review.addReviewImage(saveReviewImage(review, files));

        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseRecord> getReviews() {
        return reviewRepository.getAllBy();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseRecord> getReviewsByUserId(String userId) {
        return reviewRepository.getAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseRecord> getReviewsByBookId(Long bookId) {
        return reviewRepository.getAllByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public ReviewResponseRecord getReview(Long reviewId) {
        return reviewRepository.getByReviewId(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException(reviewId));
    }

    @Transactional(readOnly = true)
    public ReviewResponseRecord getReview(Long reviewId, String userId) {
        return reviewRepository.getByReviewIdAndUserId(reviewId, userId)
                .orElseThrow(()-> new ReviewNotFoundException(reviewId));
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, List<MultipartFile> files) {
        Review review = findWithImageByReviewId(reviewId);

        List<String> deleteKeys = review.update(reviewUpdateRequest.toCommand());
        fileService.deleteFiles(deleteKeys);

        review.addReviewImage(saveReviewImage(review, files));
    }

    @Transactional
    public void updateReview(Long reviewId, String userId, ReviewUpdateRequest reviewUpdateRequest, List<MultipartFile> files) {
        Review review = findWithImageByReviewIdAndUserId(reviewId, userId);

        List<String> deleteKeys = review.update(reviewUpdateRequest.toCommand());
        fileService.deleteFiles(deleteKeys);

        review.addReviewImage(saveReviewImage(review, files));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        List<String> imageKeys = reviewRepository.getImageUrlsByReviewId(reviewId);
        fileService.deleteFiles(imageKeys);

        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void deleteReview(Long reviewId, String userId) {
        List<String> imageKeys = reviewRepository.getImageUrlsByReviewIdAndUserId(reviewId, userId);
        fileService.deleteFiles(imageKeys);

        reviewRepository.deleteByReviewIdAndUserId(reviewId, userId);
    }

    private Review findWithImageByReviewId(Long reviewId) {
        return reviewRepository.findWithImageByReviewId(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException(reviewId));
    }

    private Review findWithImageByReviewIdAndUserId(Long reviewId, String userId) {
        return reviewRepository.findWithImageByReviewIdAndUserId(reviewId, userId)
                .orElseThrow(()-> new ReviewNotFoundException(reviewId));
    }

    private List<Image> saveReviewImage(Review review, List<MultipartFile> files) {
        List<String> images = fileService.uploadFiles(files);
        List<Image> reviewImages = new ArrayList<>(images.size());

        for (String image : images) {
            Image reviewImage = new Image(0, image, review);
            reviewImages.add(reviewImage);
        }

        return reviewImages;
    }

}