package shop.dodream.book.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.dodream.book.dto.BookRegisterRequest;
import shop.dodream.book.dto.BookUpdateRequest;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.annotation.ValidatedFiles;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookAdminListResponseRecord;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.service.BookService;

import java.util.List;


@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
@Tag(name = "Admin Book", description = "관리자용 도서 API")
public class AdminBookController {
    private final BookService bookService;


    @Operation(summary = "알라딘 도서 조회 API", description = "책 제목이나 저자등으로 도서를 검색해 알라딘 책 정보를 조회합니다.")
    @GetMapping("/aladdin-search")
    public AladdinBookSearchResult getAladdinBookList(@RequestParam("query") String query,
                                                  @RequestParam(value = "size", defaultValue = "25") int size,
                                                  @RequestParam(value = "page", defaultValue = "1") int page) {
        return bookService.getAladdinBookList(query, size, page);
    }


    @Operation(summary = "알라딘 도서 등록", description = "알라딘 API에서 검색한 도서를 등록합니다.")
    @PostMapping("/aladdin-api")
    public ResponseEntity<Void> registerFromAladdin(@RequestBody BookRegisterRequest request){
        bookService.registerFromAladdin(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "도서 직접 등록", description = "새로운 도서를 직접 등록합니다.")
    @PostMapping()
    public ResponseEntity<Void> registerBook(@Valid @RequestPart("book") BookRegisterRequest bookRegisterRequest,
                                             @ValidatedFiles @RequestPart(value = "files", required = false) List<MultipartFile> files){
        bookService.registerBookDirect(bookRegisterRequest, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "도서 전체 조회(페이징)", description = "등록된 모든 도서를 페이징하여 조회합니다.")
    @GetMapping
    public Page<BookAdminListResponseRecord> getAllBooks(Pageable pageable){
        return bookService.getAllBooks(pageable);
    }

    @Operation(summary = "도서 상세 조회 (관리자)", description = "도서 ID를 기준으로 상세 정보를 조회합니다.")
    @GetMapping("/{book-id}")
    public BookDetailResponse getBookById(@PathVariable("book-id") Long bookId){
        return bookService.getBookByIdForAdmin(bookId);
    }

    @Operation(summary = "도서 정보 수정", description = "도서의 정보를 수정합니다.")
    @PutMapping("/{book-id}")
    public ResponseEntity<Void> updateBook(@PathVariable("book-id") Long bookId,
                                           @Validated @RequestPart("book") BookUpdateRequest request,
                                           @ValidatedFiles @RequestPart(value = "files", required = false)List<MultipartFile> files){
        bookService.updateBook(bookId, request, files);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "도서 삭제 (논리 삭제)", description = "도서 ID를 기준으로 도서를 논리 삭제합니다.")
    @DeleteMapping("/{book-id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("book-id") Long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "도서 isbn 으로 도서 조회", description = "도서 isbn으로 도서 조회합니다.")
    @GetMapping("/isbn/{isbn}")
    public BookItemResponse getBookByIsbn(@PathVariable("isbn") String isbn){
        return bookService.getBookByIsbn(isbn);
    }



}
