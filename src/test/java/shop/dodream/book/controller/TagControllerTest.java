package shop.dodream.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

        mockMvc.perform(post("/admin/tags")
                .param("newTagName", newTagName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tagId").value(newTagId))
                .andExpect(jsonPath("$.tagName").value(newTagName));

        verify(tagService, times(1)).createTag(newTagName);
    }

    @Test
    @DisplayName("단일 태그 조회 테스트")
    void getTag() throws Exception {
        Long tagId = 1L;
        String tagName = "단일 조회태그";

        when(tagService.getTag(tagId)).thenReturn(new TagResponse(tagId, tagName));

        mockMvc.perform(get("/public/tags/" + tagId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId").value(tagId))
                .andExpect(jsonPath("$.tagName").value(tagName));
        verify(tagService, times(1)).getTag(tagId);
    }


    @Test
    @DisplayName("전체 태그 조회 테스트 - 페이징 없음")
    void getTagsWithoutPaging() throws Exception {
        List<TagResponse> tags = List.of(
                new TagResponse(1L, "조회 태그1"),
                new TagResponse(2L, "조회 태그2")
        );

        when(tagService.getTags()).thenReturn(tags);

        mockMvc.perform(get("/public/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // 최상위가 배열인지 확인
                .andExpect(jsonPath("$[0].tagId").value(1L))
                .andExpect(jsonPath("$[0].tagName").value("조회 태그1"))
                .andExpect(jsonPath("$[1].tagId").value(2L))
                .andExpect(jsonPath("$[1].tagName").value("조회 태그2"));

        verify(tagService, times(1)).getTags();
    }




    @Test
    @DisplayName("태그 수정")
    void updateTag() throws Exception {
        Long newTagId = 1L;
        String newTagName = "수정 태그";

        when(tagService.updateTag(newTagId, newTagName)).thenReturn(new TagResponse(newTagId, newTagName));
        mockMvc.perform(put("/admin/tags/{tag-id}", newTagId)
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

        mockMvc.perform(delete("/admin/tags/" + newTagId))
                .andExpect(status().isNoContent());

    }
}
