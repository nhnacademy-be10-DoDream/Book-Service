package shop.dodream.book.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.BookCountDecreaseRequest;
import shop.dodream.book.dto.BookCountIncreaseRequest;
import shop.dodream.book.service.BookService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookInternalController.class)
public class BookInternalControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;



    @Test
    @DisplayName("도서 재고 감소 - 잘못된 요청 (bookCount < 1")
    void decreaseBookCount() throws Exception {
        BookCountDecreaseRequest invalidRequest = new BookCountDecreaseRequest(1L, 0L);


        mockMvc.perform(post("/public/books/internal/decrease-bookCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("도서 재고 추가")
    void increaseBookCount() throws Exception{

        BookCountIncreaseRequest request = new BookCountIncreaseRequest(1L, 3L);

        mockMvc.perform(post("/public/books/internal/increase-bookCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).increaseBookCount(any(BookCountIncreaseRequest.class));


    }
}
