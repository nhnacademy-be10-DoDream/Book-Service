package shop.dodream.book.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookLikeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(BookLikeController.class)
class BookLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookLikeService bookLikeService;


    @Test
    @DisplayName("사용자 도서 좋아요 등록")
    void registerBookLike() throws Exception {
        Mockito.doNothing().when(bookLikeService).registerBookLike(anyLong(), anyString());

        mockMvc.perform(post("/likes/me/books/1")
                .header("X-USER-ID", "user1"))
                .andExpect(status().isOk());

        verify(bookLikeService, times(1)).registerBookLike(1L,"user1");
    }

    @Test
    @DisplayName("사용자의 도서 좋아요 여부 조회")
    void bookLikeFindMe() throws Exception{
        Mockito.when(bookLikeService.bookLikeFindMe(anyLong(), anyString())).thenReturn(true);

        mockMvc.perform(get("/likes/me/books/1")
                .header("X-USER-ID","user1"))
                .andExpect(status().isOk());

        verify(bookLikeService, times(1)).bookLikeFindMe(1L, "user1");
    }

    @Test
    @DisplayName("사용자의 도서 좋아요 취소")
    void bookLikeDelete() throws Exception{
        Mockito.doNothing().when(bookLikeService).bookLikeDelete(anyLong(), anyString());

        mockMvc.perform(delete("/likes/me/books/1")
                .header("X-USER-ID", "user1"))
                .andExpect(status().isOk());

        verify(bookLikeService, times(1)).bookLikeDelete(1L, "user1");
    }

    @Test
    @DisplayName("사용자가 좋아요한 도서 목록 조회")
    void getLikeBooks() throws Exception {
        BookListResponseRecord record = new BookListResponseRecord(
                1L, "제목", "저자", "1234567890123",
                20000L, 15000L, "http://example.com/book/1"
        );

        Mockito.when(bookLikeService.getLikedBooksByUserId(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(record)));

        mockMvc.perform(get("/likes/me/books")
                        .header("X-USER-ID", "user1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookId").value(1))
                .andExpect(jsonPath("$.content[0].title").value("제목"))
                .andExpect(jsonPath("$.content[0].author").value("저자"))
                .andExpect(jsonPath("$.content[0].isbn").value("1234567890123"))
                .andExpect(jsonPath("$.content[0].regularPrice").value(20000))
                .andExpect(jsonPath("$.content[0].salePrice").value(15000))
                .andExpect(jsonPath("$.content[0].bookUrl").value("http://example.com/book/1"));

        verify(bookLikeService, times(1)).getLikedBooksByUserId("user1", PageRequest.of(0, 10));


    }

    @Test
    @DisplayName("도서 좋아요 수 조회")
    void getBookLikeCount() throws Exception{
        Mockito.when(bookLikeService.getBookLikeCount(anyLong())).thenReturn(5L);

        mockMvc.perform(get("/books/1/likes/count"))
                .andExpect(status().isOk());

        verify(bookLikeService, times(1)).getBookLikeCount(1L);
    }


}
