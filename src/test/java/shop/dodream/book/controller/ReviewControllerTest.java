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
import shop.dodream.book.dto.ReviewCreateRequest;
import shop.dodream.book.dto.ReviewUpdateRequest;
import shop.dodream.book.dto.projection.ReviewResponseRecord;
import shop.dodream.book.dto.projection.ReviewSummaryResponse;
import shop.dodream.book.service.impl.ReviewServiceImpl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @MockBean
    private ReviewServiceImpl reviewService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final ReviewResponseRecord dummyReview = new ReviewResponseRecord(
            1L,
            (short) 5,
            "ㅋㅋㅋㅋz",
            ZonedDateTime.now(),
            101L,
            "userId",
            List.of("image1.jpg", "image2.jpg")
    );

    @Test
    void getReviews() throws Exception {
        Mockito.when(reviewService.getReviewsByBookId(Mockito.anyLong(), Mockito.any())).thenReturn(new PageImpl<>(List.of(dummyReview)));

        mockMvc.perform(get("/public/books/1/reviews")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId").value(1L))
                .andExpect(jsonPath("$.content[0].rating").value(5))
                .andExpect(jsonPath("$.content[0].content").value("ㅋㅋㅋㅋz"));

        Mockito.verify(reviewService, Mockito.times(1)).getReviewsByBookId(Mockito.anyLong(), Mockito.any());
    }

    @Test
    void createReview() throws Exception {
        ReviewCreateRequest request = new ReviewCreateRequest((short) 5, "노잼입니다.");
        MockMultipartFile reviewPart = new MockMultipartFile("review", "", "application/json", objectMapper.writeValueAsBytes(request));
        MockMultipartFile filePart = new MockMultipartFile("files", "test.jpg", "image/jpeg", "test image".getBytes());

        mockMvc.perform(multipart("/books/1/reviews")
                        .file(reviewPart)
                        .file(filePart)
                        .header("X-USER-ID", "testUser"))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));

        Mockito.verify(reviewService, Mockito.times(1)).createReview(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void testGetReviews() throws Exception {
        Mockito.when(reviewService.getReviews(Mockito.anyString(), Mockito.any())).thenReturn(new PageImpl<>(List.of(dummyReview)));

        mockMvc.perform(get("/reviews/me")
                        .header("X-USER-ID", "testUser")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reviewId").value(1L))
                .andExpect(jsonPath("$.content[0].userId").value("userId"));

        Mockito.verify(reviewService, Mockito.times(1)).getReviews(Mockito.anyString(), Mockito.any());
    }

    @Test
    void getReview() throws Exception {
        Mockito.when(reviewService.getReview(Mockito.anyLong(), Mockito.any())).thenReturn(dummyReview);

        mockMvc.perform(get("/reviews/me/1")
                        .header("X-USER-ID", "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.content").value("ㅋㅋㅋㅋz"))
                .andExpect(jsonPath("$.userId").value("userId"));

        Mockito.verify(reviewService, Mockito.times(1)).getReview(Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    void updateReview() throws Exception {
        ReviewUpdateRequest request = new ReviewUpdateRequest((short) 4, "수정~~~", Set.of());
        MockMultipartFile reviewPart = new MockMultipartFile("review", "", "application/json", objectMapper.writeValueAsBytes(request));
        MockMultipartFile filePart = new MockMultipartFile("files", "updated.jpg", "image/jpeg", "updated image".getBytes());

        mockMvc.perform(multipart(HttpMethod.PUT, "/reviews/me/1")
                        .file(reviewPart)
                        .file(filePart)
                        .header("X-USER-ID", "testUser"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        Mockito.verify(reviewService, Mockito.times(1)).updateReview(Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void deleteReview() throws Exception {
        mockMvc.perform(delete("/reviews/me/1")
                        .header("X-USER-ID", "testUser"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        Mockito.verify(reviewService, Mockito.times(1)).deleteReview(Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    void getReviewSummary() throws Exception {
        ReviewSummaryResponse response = new ReviewSummaryResponse(1.1, 102L);

        Mockito.when(reviewService.getReviewSummary(Mockito.anyLong())).thenReturn(response);

        mockMvc.perform(get("/public/reviews/1/review-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingAvg").value(1.1))
                .andExpect(jsonPath("$.reviewCount").value(102L));

        Mockito.verify(reviewService, Mockito.times(1)).getReviewSummary(Mockito.anyLong());
    }

}
