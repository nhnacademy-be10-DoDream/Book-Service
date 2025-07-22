package shop.dodream.book.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.projection.BookDetailResponse;
import shop.dodream.book.service.BookService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;


    @Test
    @DisplayName("사용자 도서 상세조회 테스트")
    void getBooksDetail() throws Exception {

        BookDetailResponse mockResponse = new BookDetailResponse(
                1L,
                "제목",
                "저자",
                "설명",
                "출판사",
                "1234567890123",
                LocalDate.now(),
                20000L,
                40000L,
                true,
                List.of("test.jpg"),
                50L
        );

        Mockito.doNothing().when(bookService).redisIncreaseViewCount(anyLong());
        Mockito.when(bookService.getBookByIdForUser(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/public/books/1"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("도서 조건 조회 - 성공")
    void getBooksByIds_success() throws Exception {
        Mockito.when(bookService.findAllByIds(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/public/books")
                .param("ids", "1","2","3"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("도서 전체 조회 - 성공")
    void getAllBooks_success() throws Exception {
        Mockito.when(bookService.getAllBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/public/books/all"))
                .andExpect(status().isOk());
    }









}
