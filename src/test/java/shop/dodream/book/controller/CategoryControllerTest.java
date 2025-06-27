package shop.dodream.book.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.service.CategoryService;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 등록")
    void createCategory() throws Exception {
//        Long categoryId = 1L;
//        String categoryName = "테스트 등록";
//        Long depth = 2L;
//        Long parentId = 3L;
//        CategoryRequest categoryRequest = new CategoryRequest(categoryName, depth, parentId);
//        CategoryResponse categoryResponse = new CategoryResponse(categoryId, categoryName, depth, parentId);
//        when(categoryService.createCategory(categoryRequest)).thenReturn(categoryResponse);
//
//        mockMvc.perform(get("/categories"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.categoryId").value(categoryId))
//                .andExpect(jsonPath("$.categoryName").value(categoryName))
//                .andExpect(jsonPath("$.depth").value(depth))
//                .andExpect(jsonPath("$.parentId").value(parentId));
//
//        verify(categoryService, times(1)).createCategory(categoryRequest);
    }

    @Test
    @DisplayName("전체 카테고리 조회")
    void getAllCategories() throws Exception {

    }

    @Test
    @DisplayName("특정 카테고리 하위 카테고리 조회")
    void getCategory() throws Exception {

    }

    @Test
    @DisplayName("카테고리 등록")
    void getCategoriesChildren() throws Exception {

    }

    @Test
    @DisplayName("특정 카테고리 연관 카테고리 전체 조회")
    void getCategoriesRelated() throws Exception {

    }

    @Test
    @DisplayName("특정 카테고리 연관 카테고리 전체 조회")
    void getCategoriesDepth() throws Exception {

    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategory() throws Exception {

    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() throws Exception {

    }
}
