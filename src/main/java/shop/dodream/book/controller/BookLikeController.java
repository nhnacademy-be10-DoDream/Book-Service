package shop.dodream.book.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookLikeCountResponse;
import shop.dodream.book.dto.BookLikeResponse;
import shop.dodream.book.dto.BookListResponse;
import shop.dodream.book.repository.BookLikeQuerydslRepository;
import shop.dodream.book.service.BookLikeService;

import java.util.List;

@RestController
@RequestMapping()
public class BookLikeController {

    @Autowired
    private BookLikeService bookLikeService;


    // 좋아요 등록
    @PostMapping("/books/{book-id}/likes")
    ResponseEntity<Void> registerBookLike(@PathVariable("book-id") Long bookId,
                                          @RequestHeader("X-USER-ID") String userId){

        bookLikeService.registerBookLike(bookId, userId);

        return ResponseEntity.ok().build();
    }

    // 로그인한 사용자 특정 도서 좋아요 여부 조회
    @GetMapping("/books/{book-id}/me")
    ResponseEntity<BookLikeResponse> bookLikeFindMe(@PathVariable("book-id") Long bookId,
                                                    @RequestHeader("X-USER-ID") String userId){
        BookLikeResponse bookLikeResponse = bookLikeService.bookLikeFindMe(bookId, userId);

        return ResponseEntity.ok(bookLikeResponse);
    }


    // 좋아요 취소(삭제)
    @DeleteMapping("/books/{book-id}/likes")
    ResponseEntity<Void> bookLikeDelete(@PathVariable("book-id") Long bookId,
                                        @RequestHeader("X-USER-ID") String userId){
        bookLikeService.bookLikeDelete(bookId, userId);

        return ResponseEntity.ok().build();
    }

    // 좋아요한 도서 목록 조회
    @GetMapping("/users/me/likes/books")
    ResponseEntity<List<BookListResponse>> getLikedBooks(@RequestHeader("X-USER-ID") String userId){
        List<BookListResponse> bookListResponses = bookLikeService.getLikedBooksByUserId(userId);

        return ResponseEntity.ok(bookListResponses);
    }


}
