package shop.dodream.book.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @Operation(summary = "도서 등록 외부 API 등록", description = "ISBN을 기준으로 도서를 등록합니다.")
    @PostMapping("/aladdin-api")
    public ResponseEntity<Void> aladdinRegisterBook(
            @RequestParam("isbn")
            @NotBlank(message = "isbn 은 필수값입니다.")
            @Pattern(regexp = "\\d{13}", message = "ISBN은 13자리 숫자여야합니다.")
            String isbn){
        bookService.registerBookByIsbn(isbn);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "도서 리스트로 isbn 등록", description = "isbn13자리 리스트로 등록합니다. ")
    @PostMapping("/aladdin-api/isbn13")
    public ResponseEntity<Void> aladdinRegisterBook13(@RequestBody IsbnListRequest isbn){

        bookService.registerBookListIsbn(isbn);
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
                                           @Validated @RequestBody BookUpdateRequest request,
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
    public BookResponse getBookByIsbn(@PathVariable("isbn") String isbn){
        return bookService.getBookByIsbn(isbn);
    }



}
