package shop.dodream.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.service.CategoryService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("카테고리 등록 테스트")
    void createCategoryTest() throws Exception {
        CategoryRequest request = new CategoryRequest("IT", 1L);
        CategoryResponse response = new CategoryResponse(1L, "IT", 1L, 1L);

        when(categoryService.createCategory(any())).thenReturn(response);

        mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("IT"));
    }

    @Test
    @DisplayName("전체 카테고리 조회 테스트")
    void getAllCategoriesTest() throws Exception {
        List<CategoryResponse> mockList = List.of(
                new CategoryResponse(1L, "IT", 1L, null),
                new CategoryResponse(2L, "History", 1L, null)
        );

        when(categoryService.getCategories()).thenReturn(mockList);

        mockMvc.perform(get("/public/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("카테고리 상세 조회 테스트")
    void getCategoryTest() throws Exception {
        CategoryResponse response = new CategoryResponse(1L, "IT", 1L, null);

        when(categoryService.getCategory(1L)).thenReturn(response);

        mockMvc.perform(get("/public/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("IT"));
    }

    @Test
    @DisplayName("카테고리 하위 조회 테스트")
    void getCategoryChildrenTest() throws Exception {
        Long parentId = 1L;
        List<CategoryTreeResponse> children = List.of(
                new CategoryTreeResponse(2L, "프론트엔드", 2L, parentId),
                new CategoryTreeResponse(3L, "백엔드", 2L, parentId)
        );

        when(categoryService.getCategoriesChildren(parentId)).thenReturn(children);

        mockMvc.perform(get("/public/categories/{category-id}/children", parentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("프론트엔드"));
    }

    @Test
    @DisplayName("카테고리 연관 트리 조회 테스트")
    void getCategoryRelatedTest() throws Exception {
        Long categoryId = 1L;
        List<CategoryTreeResponse> related = List.of(
                new CategoryTreeResponse(1L, "개발", 1L, null),
                new CategoryTreeResponse(2L, "자바", 2L, 1L)
        );

        when(categoryService.getCategoriesRelated(categoryId)).thenReturn(related);

        mockMvc.perform(get("/public/categories/{category-id}/related", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("개발"));
    }

    @Test
    @DisplayName("카테고리 경로 조회 테스트 (루트부터 특정 카테고리까지)")
    void getCategoryPathTest() throws Exception {
        Long categoryId = 3L;

        List<CategoryFlatProjection> path = List.of(
                new CategoryFlatProjection() {
                    public Long getCategoryId() { return 1L; }
                    public String getCategoryName() { return "개발"; }
                    public Long getDepth() { return 1L; }
                },
                new CategoryFlatProjection() {
                    public Long getCategoryId() { return 2L; }
                    public String getCategoryName() { return "웹"; }
                    public Long getDepth() { return 2L; }
                }
        );

        when(categoryService.getCategoriesPath(categoryId)).thenReturn(path);

        mockMvc.perform(get("/public/categories/{category-id}/path", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("개발"));
    }

    @Test
    @DisplayName("카테고리 깊이별 조회 테스트")
    void getCategoryByDepthTest() throws Exception {
        Long depth = 1L;

        List<CategoryResponse> response = List.of(
                new CategoryResponse(1L, "개발", 1L, null),
                new CategoryResponse(2L, "역사", 1L, null)
        );

        when(categoryService.getCategoriesDepth(depth)).thenReturn(response);

        mockMvc.perform(get("/public/categories/{depth}/depth", depth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("개발"));
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    void updateCategoryTest() throws Exception {
        Long categoryId = 1L;
        CategoryRequest request = new CategoryRequest("수정된 카테고리", 1L, null);
        CategoryResponse response = new CategoryResponse(categoryId, "수정된 카테고리", 1L, null);

        when(categoryService.updateCategory(anyLong(), any(CategoryRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/admin/categories/{category-id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("수정된 카테고리"))
                .andExpect(jsonPath("$.depth").value(1))
                .andExpect(jsonPath("$.parentId").doesNotExist());

        verify(categoryService, times(1)).updateCategory(eq(categoryId), any(CategoryRequest.class));
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void deleteCategoryTest() throws Exception {
        mockMvc.perform(delete("/admin/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(1L);
    }
}
