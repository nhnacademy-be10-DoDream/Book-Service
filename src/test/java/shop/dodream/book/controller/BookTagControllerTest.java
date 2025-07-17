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
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.service.BookTagService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookTagController.class)
class BookTagControllerTest {
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

        mockMvc.perform(post("/admin/books/{book-id}/tags/{tag-id}", newBookId, newTagId))
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

        mockMvc.perform(get("/public/books/{book-id}/tags", newBookId))
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

        BookListResponseRecord book1 = new BookListResponseRecord(
                10L, "테스트 도서제목", "테스트 도서저자", "11111111", 101L, 201L, "테스트 URL"
        );
        BookListResponseRecord book2 = new BookListResponseRecord(
                20L, "테스트 도서제목2", "테스트 도서저자2", "22222222", 102L, 202L, "테스트 URL"
        );

        List<BookListResponseRecord> content = List.of(book1, book2);
        Page<BookListResponseRecord> bookListResponseRecords = new PageImpl<>(content);

        Pageable pageable = PageRequest.of(0, 10);

        when(bookTagService.getBooksByTagId(newTagId, pageable)).thenReturn(bookListResponseRecords);

        mockMvc.perform(get("/public/tags/{tag-id}/books", newTagId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookId").value(10L))
                .andExpect(jsonPath("$.content[0].title").value("테스트 도서제목"))
                .andExpect(jsonPath("$.content[0].author").value("테스트 도서저자"))
                .andExpect(jsonPath("$.content[0].isbn").value("11111111"))
                .andExpect(jsonPath("$.content[0].regularPrice").value(101))
                .andExpect(jsonPath("$.content[0].salePrice").value(201))
                .andExpect(jsonPath("$.content[0].bookUrl").value("테스트 URL"))
                .andExpect(jsonPath("$.content[1].bookId").value(20L))
                .andExpect(jsonPath("$.content[1].title").value("테스트 도서제목2"))
                .andExpect(jsonPath("$.content[1].author").value("테스트 도서저자2"))
                .andExpect(jsonPath("$.content[1].isbn").value("22222222"))
                .andExpect(jsonPath("$.content[1].regularPrice").value(102))
                .andExpect(jsonPath("$.content[1].salePrice").value(202))
                .andExpect(jsonPath("$.content[1].bookUrl").value("테스트 URL"));

        verify(bookTagService, times(1)).getBooksByTagId(newTagId, pageable);
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

        mockMvc.perform(put("/admin/books/{book-id}/tags/{tag-id}", newBookId, newTagId)
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

        mockMvc.perform(delete("/admin/books/{book-id}/tags/{tag-id}", newBookId, newTagId))
                .andExpect(status().isNoContent());
    }
}
