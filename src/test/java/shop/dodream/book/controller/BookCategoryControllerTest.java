package shop.dodream.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.BookWithCategoriesResponse;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.IdsListRequest;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookCategoryService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookCategoryController.class)
class BookCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCategoryService bookCategoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("도서 카테고리 추가")
    void registerCategory() throws Exception {
        Long bookId = 1L;
        IdsListRequest request = new IdsListRequest(List.of(10L, 20L));

        BookWithCategoriesResponse response = new BookWithCategoriesResponse(bookId, List.of(
                new CategoryResponse(10L, "소설", 1L, null),
                new CategoryResponse(20L, "스릴러", 2L, 10L)
        ));

        when(bookCategoryService.registerCategory(eq(bookId), any())).thenReturn(response);

        mockMvc.perform(post("/admin/books/{book-id}/categories", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.categories[0].categoryId").value(10L))
                .andExpect(jsonPath("$.categories[1].categoryId").value(20L));
    }

    @Test
    @DisplayName("해당 도서의 카테고리 트리 조회")
    void getCategoriesByBookId() throws Exception {
        Long bookId = 1L;
        List<CategoryTreeResponse> mockList = List.of(
                new CategoryTreeResponse(10L, "문학", 1L, null)
        );
        when(bookCategoryService.getCategoriesByBookId(bookId)).thenReturn(mockList);

        mockMvc.perform(get("/public/books/{book-id}/categories", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value(10L))
                .andExpect(jsonPath("$[0].categoryName").value("문학"));
    }

    @Test
    @DisplayName("해당 도서의 카테고리 플랫 조회")
    void getFlatCategoriesByBookId() throws Exception {
        Long bookId = 1L;

        List<CategoryResponse> response = List.of(
                new CategoryResponse(10L, "문학", 1L, null),
                new CategoryResponse(11L, "시", 2L, 10L)
        );

        when(bookCategoryService.getFlatCategoriesByBookId(bookId)).thenReturn(response);

        mockMvc.perform(get("/public/books/{book-id}/categories/flat", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value(10L))
                .andExpect(jsonPath("$[1].categoryId").value(11L));
    }

    @Test
    @DisplayName("해당 카테고리의 도서 조회(페이징)")
    void getBooksByCategoryId() throws Exception {
        Long categoryId = 100L;

        BookListResponseRecord book1 = new BookListResponseRecord(1L, "제목1", "저자1", "1234", 1000L, 900L, "url1");
        BookListResponseRecord book2 = new BookListResponseRecord(2L, "제목2", "저자2", "5678", 1200L, 1100L, "url2");

        Page<BookListResponseRecord> page = new PageImpl<>(List.of(book1, book2), PageRequest.of(0, 15), 2);

        when(bookCategoryService.getBooksByCategoryId(eq(categoryId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/public/categories/{category-id}/books", categoryId)
                        .param("page", "0")
                        .param("size", "15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookId").value(1L))
                .andExpect(jsonPath("$.content[1].bookId").value(2L));

    }

    @Test
    @DisplayName("도서 카테고리 단일 정보 수정")
    void updateCategoryByBook() throws Exception {
        Long bookId = 1L;
        Long oldCategoryId = 10L;
        Long newCategoryId = 20L;

        when(bookCategoryService.updateCategoryByBook(bookId, oldCategoryId, newCategoryId)).thenReturn(newCategoryId);

        mockMvc.perform(put("/admin/books/{book-id}/categories/{category-id}", bookId, oldCategoryId)
                        .param("new-category-id", newCategoryId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("도서 카테고리 삭제")
    void deleteCategoriesByBook() throws Exception {
        Long bookId = 1L;
        IdsListRequest request = new IdsListRequest(List.of(10L, 20L));

        doNothing().when(bookCategoryService).deleteCategoriesByBook(bookId, request);

        mockMvc.perform(delete("/admin/books/{book-id}/categories", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

    }
}
