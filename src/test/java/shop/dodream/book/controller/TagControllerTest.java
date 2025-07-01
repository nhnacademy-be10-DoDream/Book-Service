package shop.dodream.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.service.TagService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("태그 생성 테스트")
    void createTag() throws Exception {
        Long newTagId = 1L;
        String newTagName = "생성 태그";

        when(tagService.createTag(newTagName)).thenReturn(new TagResponse(newTagId, newTagName));

        mockMvc.perform(post("/tags")
                .param("newTagName", newTagName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tagId").value(newTagId))
                .andExpect(jsonPath("$.tagName").value(newTagName));

        verify(tagService, times(1)).createTag(newTagName);
    }

    @Test
    @DisplayName("전체 태그 조회 테스트")
    void getTag() throws Exception {
        List<TagResponse> tags = List.of(
                new TagResponse(1L, "조회 태그1"),
                new TagResponse(2L, "조회 태그2")
        );
        Page<TagResponse> page = new PageImpl<>(tags);
        when(tagService.getTags(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/public/tags")
                .param("page", "0")
                .param("size", "2")
                .param("sort", "tagName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].tagId").value(1L))
                .andExpect(jsonPath("$.content[0].tagName").value("조회 태그1"))
                .andExpect(jsonPath("$.content[1].tagId").value(2L))
                .andExpect(jsonPath("$.content[1].tagName").value("조회 태그2"));

        verify(tagService, times(1)).getTags(any(Pageable.class));
    }

    @Test
    @DisplayName("태그 수정")
    void updateTag() throws Exception {
        Long newTagId = 1L;
        String newTagName = "수정 태그";

        when(tagService.updateTag(newTagId, newTagName)).thenReturn(new TagResponse(newTagId, newTagName));
        mockMvc.perform(put("/tags/{tag-id}", newTagId)
                .param("newTagName", newTagName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(newTagId))
                .andExpect(jsonPath("$.tagName").value(newTagName));

        verify(tagService, times(1)).updateTag(newTagId, newTagName);
    }

    @Test
    @DisplayName("태그 삭제")
    void deleteTag() throws Exception {
        Long newTagId = 1L;

        doNothing().when(tagService).deleteTag(newTagId);

        mockMvc.perform(delete("/tags/" + newTagId))
                .andExpect(status().isNoContent());

    }
}
