package shop.dodream.book.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookLikeService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BookLikeController {
    private final BookLikeService bookLikeService;


    // 좋아요 등록
    // TODO 삭제된 도서는 좋아요 안되게끔
    @PostMapping("/books/{book-id}/likes")
    public ResponseEntity<Void> registerBookLike(@PathVariable("book-id") Long bookId,
                                          @RequestHeader("X-USER-ID") String userId){
        bookLikeService.registerBookLike(bookId, userId);
        return ResponseEntity.ok().build();
    }

    // 로그인한 사용자 특정 도서 좋아요 여부 조회
    @GetMapping("/books/{book-id}/me")
    public Boolean bookLikeFindMe(@PathVariable("book-id") Long bookId,
                                                    @RequestHeader("X-USER-ID") String userId){

        return bookLikeService.bookLikeFindMe(bookId, userId);
    }


    // 좋아요 취소(삭제)
    @DeleteMapping("/books/{book-id}/likes")
    public ResponseEntity<Void> bookLikeDelete(@PathVariable("book-id") Long bookId,
                                        @RequestHeader("X-USER-ID") String userId){
        bookLikeService.bookLikeDelete(bookId, userId);

        return ResponseEntity.ok().build();
    }

    // 좋아요한 도서 목록 조회
    @GetMapping("/likes/me")
    public List<BookListResponseRecord> getLikedBooks(@RequestHeader("X-USER-ID") String userId){
        return bookLikeService.getLikedBooksByUserId(userId);
    }

    // 도서 좋아요 수 조회
    @GetMapping("/books/{book-id}/likes/count")
    public Long getBookLikeCount(@PathVariable("book-id") Long bookId){
        return bookLikeService.getBookLikeCount(bookId);
    }

}
