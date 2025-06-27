package shop.dodream.book.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.service.BookCategoryService;

@WebMvcTest(BookCategoryController.class)
public class BookCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCategoryService BookCategoryService;

    @Test
    @DisplayName("도서 카테고리 추가")
    void registerCategory() throws Exception {

    }

    @Test
    @DisplayName("해당 도서의 카테고리 조회")
    void getCategoriesByBookId() throws Exception {

    }

    @Test
    @DisplayName("해당 카테고리의 도서 조회")
    void getBooksByCategoryId() throws Exception {

    }

    @Test
    @DisplayName("도서 카테고리 단일 정보 수정")
    void updateCategoryByBook() throws Exception {

    }

    @Test
    @DisplayName("도서 카테고리 삭제")
    void deleteCategoriesByBook() throws Exception {

    }
}
