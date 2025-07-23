package shop.dodream.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.ReviewUpdateRequest;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.service.ReviewService;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminReviewController.class)
class AdminReviewControllerTest {
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final ReviewResponseRecord dummyReview = new ReviewResponseRecord(
            1L,
            (short) 5,
            "ㅜㅜㅜㅜㅜ",
            ZonedDateTime.now(),
            101L,
            "userId",
            List.of("image1.jpg", "image2.jpg")
    );

    @Test
    void getReviews() throws Exception {
        Mockito.when(reviewService.getReviews(Mockito.anyString(), Mockito.any())).thenReturn(new PageImpl<>(List.of(dummyReview)));

        mockMvc.perform(get("/admin/reviews")
                        .param("user-id", "testUser")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId").value(1L))
                .andExpect(jsonPath("$.content[0].rating").value(5))
                .andExpect(jsonPath("$.content[0].userId").value("userId"));

        Mockito.verify(reviewService, Mockito.times(1)).getReviews(Mockito.anyString(), Mockito.any());
    }

    @Test
    void getReview() throws Exception {
        Mockito.when(reviewService.getReview(Mockito.anyLong())).thenReturn(dummyReview);

        mockMvc.perform(get("/admin/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.content").value("ㅜㅜㅜㅜㅜ"))
                .andExpect(jsonPath("$.userId").value("userId"));

        Mockito.verify(reviewService, Mockito.times(1)).getReview(Mockito.anyLong());
    }

    @Test
    void updateReview() throws Exception {
        ReviewUpdateRequest request = new ReviewUpdateRequest((short) 4, "수정했다고 수정", Set.of());
        MockMultipartFile reviewPart = new MockMultipartFile("review", "", "application/json", objectMapper.writeValueAsBytes(request));
        MockMultipartFile filePart = new MockMultipartFile("files", "updated.jpg", "image/jpeg", "updated image".getBytes());

        mockMvc.perform(multipart(HttpMethod.PUT, "/admin/reviews/1")
                        .file(reviewPart)
                        .file(filePart))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        Mockito.verify(reviewService, Mockito.times(1)).updateReview(Mockito.anyLong(), Mockito.any(), Mockito.any());
    }

    @Test
    void deleteReview() throws Exception {
        mockMvc.perform(delete("/admin/reviews/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        Mockito.verify(reviewService, Mockito.times(1)).deleteReview(Mockito.anyLong());
    }
}
