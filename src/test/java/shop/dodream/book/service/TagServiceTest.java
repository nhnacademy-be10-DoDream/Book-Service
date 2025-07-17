package shop.dodream.book.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.dodream.book.dto.TagResponse;
import shop.dodream.book.entity.Tag;
import shop.dodream.book.exception.TagNotFoundException;
import shop.dodream.book.repository.BookTagRepository;
import shop.dodream.book.repository.TagRepository;
import shop.dodream.book.service.impl.TagServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceTest {

    private TagRepository tagRepository;
    private BookTagRepository bookTagRepository;
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        tagRepository = mock(TagRepository.class);
        bookTagRepository = mock(BookTagRepository.class);
        tagService = new TagServiceImpl(tagRepository, bookTagRepository);
    }

    @Test
    @DisplayName("태그 생성 테스트")
    void createTagTest() {
        Long tagId = 1L;
        String tagName = "IT";
        Tag savedTag = new Tag(tagId, tagName);

        when(tagRepository.save(any(Tag.class))).thenReturn(savedTag);

        TagResponse result = tagService.createTag(tagName);

        assertThat(result.getTagId()).isEqualTo(tagId);
        assertThat(result.getTagName()).isEqualTo(tagName);
    }

    @Test
    @DisplayName("태그 단일 조회 테스트")
    void getTagTest() {
        Long tagId = 1L;
        String tagName = "IT";
        Tag savedTag = new Tag(tagId, tagName);

        when(tagRepository.findById(tagId)).thenReturn(Optional.of(savedTag));

        TagResponse result = tagService.getTag(tagId);

        assertThat(result).isNotNull();
        assertThat(result.getTagName()).isEqualTo(tagName);
        verify(tagRepository, times(1)).findById(tagId);
    }

    @Test
    @DisplayName("태그 전체 조회 테스트")
    void getTagsTest() {
        List<Tag> tagList = Arrays.asList(
                new Tag(1L, "IT"),
                new Tag(2L, "역사"),
                new Tag(3L, "만화")
        );

        when(tagRepository.findAll()).thenReturn(tagList);

        List<TagResponse> result = tagService.getTags();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getTagName()).isEqualTo("IT");
    }

    @Test
    @DisplayName("태그 수정 테스트")
    void updateTagTest() {
        Tag tag = new Tag(1L, "Old Name");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        TagResponse result = tagService.updateTag(1L, "New Name");

        assertThat(result.getTagId()).isEqualTo(1L);
        assertThat(result.getTagName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("태그 수정 시 존재하지 않는 경우 예외 발생")
    void updateTagNotFoundTest() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.updateTag(1L, "New Name"))
                .isInstanceOf(TagNotFoundException.class);
    }

    @Test
    @DisplayName("태그 삭제 테스트")
    void deleteTagTest() {
        Tag tag = new Tag(1L, "Sample");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        tagService.deleteTag(1L);

        verify(tagRepository, times(1)).delete(tag);
    }

    @Test
    @DisplayName("태그 삭제 시 존재하지 않는 경우 예외 발생")
    void deleteTagNotFoundTest() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tagService.deleteTag(1L))
                .isInstanceOf(TagNotFoundException.class);
    }
}
