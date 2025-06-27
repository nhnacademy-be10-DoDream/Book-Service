package shop.dodream.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookTagService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookTagController.class)
public class BookTagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookTagService bookTagService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("해당 도서에 태그 등록")
    void registerTag() throws Exception {
        Long newBookId = 10L;
        Long newTagId = 1L;
        String newTagName = "도서 태그 테스트";

        BookWithTagResponse bookWithTagResponse = new BookWithTagResponse(newBookId, new TagResponse(newTagId, newTagName));
        when(bookTagService.registerTag(newBookId, newTagId)).thenReturn(bookWithTagResponse);

        mockMvc.perform(post("/books/{book-id}/tags/{tag-id}", newBookId, newTagId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(newBookId))
                .andExpect(jsonPath("$.tag.tagId").value(newTagId))
                .andExpect(jsonPath("$.tag.tagName").value(newTagName));

        verify(bookTagService, times(1)).registerTag(newBookId, newTagId);

    }

    @Test
    @DisplayName("해당 도서의 태그 조회")
    void getTagsByBookId() throws Exception {
        Long newBookId = 10L;
        Long tagId1 = 1L;
        Long tagId2 = 2L;
        String tagName1 = "태그1";
        String tagName2 = "태그2";

        List<TagResponse> tagResponses = List.of(
                new TagResponse(tagId1, tagName1),
                new TagResponse(tagId2, tagName2)
        );


        BookWithTagsResponse bookWithTagsResponse = new BookWithTagsResponse(newBookId, tagResponses);
        when(bookTagService.getTagsByBookId(newBookId)).thenReturn(bookWithTagsResponse);

        mockMvc.perform(get("/books/{book-id}/tags", newBookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(newBookId))
                .andExpect(jsonPath("$.tags[0].tagId").value(tagId1))
                .andExpect(jsonPath("$.tags[0].tagName").value(tagName1))
                .andExpect(jsonPath("$.tags[1].tagId").value(tagId2))
                .andExpect(jsonPath("$.tags[1].tagName").value(tagName2));

        verify(bookTagService, times(1)).getTagsByBookId(newBookId);
    }

    @Test
    @DisplayName("해당 태그의 도서 조회")
    void getBooksByTagId() throws Exception {

        Long newTagId = 1L;
        Long newBookId1 = 10L;
        String newTitle1 = "테스트 도서제목";
        String newAuthor1 = "테스트 도서저자";
        String newIsbn1 = "11111111";
        Long newRegularPrice1 = 101L;
        Long newSalePrice1 = 201L;
        String newBookUrl1 = "테스트 URL";

        Long newBookId2 = 20L;
        String newTitle2 = "테스트 도서제목2";
        String newAuthor2 = "테스트 도서저자2";
        String newIsbn2 = "22222222";
        Long newRegularPrice2 = 102L;
        Long newSalePrice2 = 202L;
        String newBookUrl2 = "테스트 URL";

        List<BookListResponseRecord> bookListResponseRecords = List.of(
                new BookListResponseRecord(newBookId1, newTitle1, newAuthor1, newIsbn1, newRegularPrice1, newSalePrice1, newBookUrl1),
                new BookListResponseRecord(newBookId2, newTitle2, newAuthor2, newIsbn2, newRegularPrice2, newSalePrice2, newBookUrl2)
        );

        when(bookTagService.getBooksByTagId(newTagId)).thenReturn(bookListResponseRecords);
        mockMvc.perform(get("/tags/{tag-id}/books", newTagId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(newBookId1))
                .andExpect(jsonPath("$[0].title").value(newTitle1))
                .andExpect(jsonPath("$[0].author").value(newAuthor1))
                .andExpect(jsonPath("$[0].isbn").value(newIsbn1))
                .andExpect(jsonPath("$[0].regularPrice").value(newRegularPrice1))
                .andExpect(jsonPath("$[0].salePrice").value(newSalePrice1))
                .andExpect(jsonPath("$[0].bookUrl").value(newBookUrl1))
                .andExpect(jsonPath("$[1].bookId").value(newBookId2))
                .andExpect(jsonPath("$[1].title").value(newTitle2))
                .andExpect(jsonPath("$[1].author").value(newAuthor2))
                .andExpect(jsonPath("$[1].isbn").value(newIsbn2))
                .andExpect(jsonPath("$[1].regularPrice").value(newRegularPrice2))
                .andExpect(jsonPath("$[1].salePrice").value(newSalePrice2))
                .andExpect(jsonPath("$[1].bookUrl").value(newBookUrl2));


        verify(bookTagService, times(1)).getBooksByTagId(newTagId);
    }

    @Test
    @DisplayName("해당 도서의 태그 수정")
    void updateTagsByBook() throws Exception {
        Long newBookId = 10L;
        Long newTagId = 1L;
        Long registerTagId = 2L;
        String newTagName = "도서 태그 테스트";

        BookWithTagResponse bookWithTagResponse = new BookWithTagResponse(newBookId, new TagResponse(newTagId, newTagName));
        when(bookTagService.updateTagByBook(newBookId, newTagId, registerTagId)).thenReturn(bookWithTagResponse);

        mockMvc.perform(patch("/books/{book-id}/tags/{tag-id}", newBookId, newTagId)
                .param("newTagId", String.valueOf(registerTagId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(newBookId))
                .andExpect(jsonPath("$.tag.tagId").value(newTagId))
                .andExpect(jsonPath("$.tag.tagName").value(newTagName));

        verify(bookTagService, times(1)).updateTagByBook(newBookId, newTagId, registerTagId);
    }

    @Test
    @DisplayName("해당 도서의 태그 삭제")
    void deleteTagByBook() throws Exception {
        Long newBookId = 10L;
        Long newTagId = 1L;
        doNothing().when(bookTagService).deleteTagByBook(newBookId, newTagId);

        mockMvc.perform(delete("/books/{book-id}/tags/{tag-id}", newBookId, newTagId))
                .andExpect(status().isNoContent());
    }
}
