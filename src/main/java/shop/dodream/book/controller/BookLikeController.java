package shop.dodream.book.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookLikeService;


@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Book Like", description = "도서 좋아요 API")
public class BookLikeController {
    private final BookLikeService bookLikeService;



    @Operation(summary = "도서 좋아요 등록", description = "도서에 좋아요를 등록합니다.")
    @PostMapping("/books/{book-id}/likes")
    public ResponseEntity<Void> registerBookLike(@PathVariable("book-id") Long bookId,
                                          @RequestHeader("X-USER-ID") String userId){

        bookLikeService.registerBookLike(bookId, userId);
        return ResponseEntity.ok().build();
    }

    // 로그인한 사용자 특정 도서 좋아요 여부 조회
    @Operation(summary = "사용자의 도서 좋아요 여부 조회", description = "로그인한 사용자가 해당 도서를 좋아요 했는지 여부를 조회합니다.")
    @GetMapping("/books/{book-id}/me")
    public Boolean bookLikeFindMe(@PathVariable("book-id") Long bookId,
                                                    @RequestHeader("X-USER-ID") String userId){

        return bookLikeService.bookLikeFindMe(bookId, userId);
    }


    // 좋아요 취소(삭제)
    @Operation(summary = "도서 좋아요 취소", description = "사용자가 도서 좋아요를 취소합니다.")
    @DeleteMapping("/books/{book-id}/likes")
    public ResponseEntity<Void> bookLikeDelete(@PathVariable("book-id") Long bookId,
                                        @RequestHeader("X-USER-ID") String userId){
        bookLikeService.bookLikeDelete(bookId, userId);

        return ResponseEntity.ok().build();
    }

    // 좋아요한 도서 목록 조회
    @Operation(summary = "사용자가 좋아요한 도서 목록 조회", description = "로그인한 사용자가 좋아요한 도서 목록을 조회합니다.")
    @GetMapping("/likes/me")
    public Page<BookListResponseRecord> getLikedBooks(@RequestHeader("X-USER-ID") String userId, Pageable pageable){
        return bookLikeService.getLikedBooksByUserId(userId, pageable);
    }

    // 도서 좋아요 수 조회
    @Operation(summary = "도서 좋아요 수 조회", description = "해당 도서의 총 좋아요 수를 조회합니다.")
    @GetMapping("/books/{book-id}/likes/count")
    public Long getBookLikeCount(@PathVariable("book-id") Long bookId){
        return bookLikeService.getBookLikeCount(bookId);
    }

}
