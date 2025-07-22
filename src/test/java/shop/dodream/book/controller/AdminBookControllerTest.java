package shop.dodream.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.AladdinBookSearchResult;
import shop.dodream.book.dto.BookItemResponse;
import shop.dodream.book.dto.BookRegisterRequest;
import shop.dodream.book.dto.BookUpdateRequest;
import shop.dodream.book.dto.projection.BookAdminListResponseRecord;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.service.BookService;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminBookController.class)
class AdminBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("알라딘 도서 조회 API 테스트")
    void getAladdinBookList() throws Exception {
        AladdinBookSearchResult mockResult = new AladdinBookSearchResult();
        Mockito.when(bookService.getAladdinBookList(anyString(), anyInt(), anyInt()))
                .thenReturn(mockResult);

        mockMvc.perform(get("/admin/books/aladdin-search")
                        .param("query", "테스트")
                        .param("size", "10")
                        .param("page", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("알라딘 도서 등록 테스트")
    void registerFromAladdin() throws Exception{
        BookRegisterRequest request = new BookRegisterRequest(
                "안녕",
                "설명",
                "저자",
                "출판사",
                LocalDate.now(),
                "isbn",
                20000L,
                15000L,
                true,
                50L,
                "http://test.url"
        );

        mockMvc.perform(post("/admin/books/aladdin-api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("도서 직접 등록 테스트")
    void registerBookTest() throws Exception {
        BookRegisterRequest request = new BookRegisterRequest();
        request.setTitle("테스트 책");
        request.setDescription("설명");
        request.setAuthor("테스트 저자");
        request.setPublisher("테스트 출판사");
        request.setPublishedAt(LocalDate.of(2024,1, 1));
        request.setIsbn("1234567890123");
        request.setSalePrice(15000L);
        request.setRegularPrice(20000L);
        request.setIsGiftable(true);
        request.setBookCount(50L);


        MockMultipartFile jsonPart = new MockMultipartFile(
                "book",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)
        );

        MockMultipartFile imagePart = new MockMultipartFile(
                "files",
                "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake image".getBytes()
        );

        mockMvc.perform(multipart("/admin/books")
                        .file(jsonPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

    }



    @Test
    @DisplayName("도서 전체 조회 테스트")
    void getAllBooksTest() throws Exception {

        BookAdminListResponseRecord record = new BookAdminListResponseRecord(
                1L,
                "제목",
                "저자",
                "1234567890",
                20000L,
                15000L,
                "http://test.url",
                ZonedDateTime.now(),
                BookStatus.SELL
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<BookAdminListResponseRecord> page = new PageImpl<>(
                List.of(record),
                pageable,
                1
        );

        Mockito.when(bookService.getAllBooks(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/admin/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookId").value(1))
                .andExpect(jsonPath("$.content[0].title").value("제목"))
                .andExpect(jsonPath("$.content[0].author").value("저자"))
                .andExpect(jsonPath("$.content[0].isbn").value("1234567890"))
                .andExpect(jsonPath("$.content[0].regularPrice").value(20000))
                .andExpect(jsonPath("$.content[0].salePrice").value(15000))
                .andExpect(jsonPath("$.content[0].bookUrl").value("http://test.url"))
                .andExpect(jsonPath("$.content[0].status").value("SELL"))
                .andExpect(jsonPath("$.number").value(0))      // 페이지 번호
                .andExpect(jsonPath("$.size").value(10))       // 요청한 size
                .andExpect(jsonPath("$.totalElements").value(1));  // 전체 데이터 수
    }

    @Test
    @DisplayName("도서 상세 조회 (관리자) 테스트")
    void getBookById() throws Exception {
        BookDetailResponse response = new BookDetailResponse(
                1L,
                "제목",
                "저자",
                "설명",
                "출판사",
                "1234567890123",
                LocalDate.now(),
                15000L,
                20000L,
                true,
                List.of("http://example.com/book1.jpg"),
                25L,
                BookStatus.SELL,
                ZonedDateTime.now(),
                100L,
                10L
        );

        Mockito.when(bookService.getBookByIdForAdmin(1L))
                .thenReturn(response);

        mockMvc.perform(get("/admin/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.author").value("저자"))
                .andExpect(jsonPath("$.description").value("설명"))
                .andExpect(jsonPath("$.publisher").value("출판사"))
                .andExpect(jsonPath("$.isbn").value("1234567890123"))
                .andExpect(jsonPath("$.salePrice").value(15000))
                .andExpect(jsonPath("$.regularPrice").value(20000))
                .andExpect(jsonPath("$.isGiftable").value(true))
                .andExpect(jsonPath("$.bookUrls[0]").value("http://example.com/book1.jpg"))
                .andExpect(jsonPath("$.discountRate").value(25))
                .andExpect(jsonPath("$.status").value("SELL"))
                .andExpect(jsonPath("$.viewCount").value(100))
                .andExpect(jsonPath("$.bookCount").value(10))
                .andExpect(jsonPath("$.createdAt").exists());
    }


    @Test
    @DisplayName("도서 정보 수정 API 테스트")
    void updateBookTest() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setTitle("수정된 제목");
        request.setRegularPrice(25000L);
        request.setSalePrice(20000L);

        MockMultipartFile bookPart = new MockMultipartFile(
                "book",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)
        );

        mockMvc.perform(multipart("/admin/books/{book-id}", 1L)
                .file(bookPart)
                .with(requestBuilder -> {
                    requestBuilder.setMethod("PUT");
                    return requestBuilder;
                }))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService).updateBook(eq(1L), any(BookUpdateRequest.class), any());

    }

    @Test
    @DisplayName("도서 삭제 API 테스트")
    void deleteBookTest() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/admin/books/{book-id}", bookId))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService).deleteBook(eq(bookId));
    }

    @Test
    @DisplayName("도서 ISBN으로 조회 테스트")
    void getBookByIsbnTest() throws Exception {
        String isbn = "12345667890123";
        BookItemResponse response = new BookItemResponse(
                1L,
                "제목"
        );

        Mockito.when(bookService.getBookByIsbn(isbn)).thenReturn(response);

        mockMvc.perform(get("/admin/books/isbn/{isbn}", isbn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.title").value("제목"));

        Mockito.verify(bookService).getBookByIsbn(isbn);
    }


}
