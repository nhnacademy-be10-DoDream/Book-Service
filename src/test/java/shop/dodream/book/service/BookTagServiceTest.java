package shop.dodream.book.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.dodream.book.dto.BookWithTagResponse;
import shop.dodream.book.dto.BookWithTagsResponse;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookTag;
import shop.dodream.book.entity.BookTagId;
import shop.dodream.book.entity.Tag;
import shop.dodream.book.exception.BookNotFoundException;
import shop.dodream.book.exception.BookTagDuplicateException;
import shop.dodream.book.exception.BookTagNotFoundException;
import shop.dodream.book.exception.TagNotFoundException;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.BookTagRepository;
import shop.dodream.book.repository.TagRepository;
import shop.dodream.book.service.impl.BookTagServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookTagServiceTest {

    private BookRepository bookRepository;
    private BookTagRepository bookTagRepository;
    private TagRepository tagRepository;
    private BookTagServiceImpl bookTagService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookTagRepository = mock(BookTagRepository.class);
        tagRepository = mock(TagRepository.class);
        bookTagService = new BookTagServiceImpl(bookTagRepository, bookRepository, tagRepository);
    }

    @Test
    @DisplayName("책에 태그 등록")
    void registerTagTest() {
        Book book = new Book();
        Tag tag = new Tag(1L, "IT");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(false);

        BookWithTagResponse result = bookTagService.registerTag(1L, 1L);

        assertThat(result.getBookId()).isEqualTo(book.getId());
        assertThat(result.getTag().getTagId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("책에 태그 등록 - 중복 예외")
    void registerTagDuplicateTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(new Tag(1L, "IT")));
        when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> bookTagService.registerTag(1L, 1L))
                .isInstanceOf(BookTagDuplicateException.class);
    }

    @Test
    @DisplayName("책 태그 조회")
    void getTagsByBookIdTest() {
        Book book = new Book();
        List<Tag> tags = Arrays.asList(new Tag(1L, "IT"), new Tag(2L, "역사"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookTagRepository.findAllByBookId(1L)).thenReturn(tags);

        BookWithTagsResponse result = bookTagService.getTagsByBookId(1L);

        assertThat(result.getBookId()).isEqualTo(book.getId());
        assertThat(result.getTags()).hasSize(2);
    }

    @Test
    @DisplayName("태그로 책 조회")
    void getBooksByTagIdTest() {
        Page<BookListResponseRecord> page = new PageImpl<>(List.of());

        when(tagRepository.existsById(1L)).thenReturn(true);
        when(bookTagRepository.findBookListByTagId(1L, PageRequest.of(0, 10))).thenReturn(page);

        Page<BookListResponseRecord> result = bookTagService.getBooksByTagId(1L, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("태그로 책 조회 - 태그 없음")
    void getBooksByTagIdNotFoundTest() {
        when(tagRepository.existsById(1L)).thenReturn(false);

        ThrowableAssert.ThrowingCallable callable = () -> bookTagService.getBooksByTagId(1L, PageRequest.of(0, 10));

        assertThatThrownBy(callable)
                .isInstanceOf(TagNotFoundException.class);
    }

    @Test
    @DisplayName("책 태그 수정")
    void updateTagByBookTest() {
        Book book = new Book();
        Tag newTag = new Tag(2L, "새 태그");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(tagRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.findById(2L)).thenReturn(Optional.of(newTag));
        when(bookTagRepository.findByBookIdAndTagId(1L, 1L)).thenReturn(Optional.of(new BookTag(book, new Tag(1L, "기존 태그"))));
        when(bookTagRepository.existsByBookIdAndTagId(1L, 2L)).thenReturn(false);

        BookWithTagResponse result = bookTagService.updateTagByBook(1L, 1L, 2L);

        assertThat(result.getBookId()).isEqualTo(book.getId());
        assertThat(result.getTag().getTagId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("책 태그 수정 - 기존 태그 존재하지 않음")
    void updateTagByBook_TagNotFoundTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(tagRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> bookTagService.updateTagByBook(1L, 1L, 2L))
                .isInstanceOf(TagNotFoundException.class);
    }

    @Test
    @DisplayName("책 태그 수정 - 중복 예외")
    void updateTagByBookDuplicateTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(new Book()));
        when(tagRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.findById(2L)).thenReturn(Optional.of(new Tag(2L, "새 태그")));
        when(bookTagRepository.findByBookIdAndTagId(1L, 1L)).thenReturn(Optional.of(new BookTag(new Book(), new Tag(1L, "기존 태그"))));
        when(bookTagRepository.existsByBookIdAndTagId(1L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> bookTagService.updateTagByBook(1L, 1L, 2L))
                .isInstanceOf(BookTagDuplicateException.class);
    }

    @Test
    @DisplayName("책 태그 삭제")
    void deleteTagByBookTest() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.existsById(1L)).thenReturn(true);
        when(bookTagRepository.findById(any(BookTagId.class))).thenReturn(Optional.of(new BookTag(new Book(), new Tag(1L, "IT"))));

        bookTagService.deleteTagByBook(1L, 1L);

        verify(bookTagRepository, times(1)).delete(any(BookTag.class));
    }

    @Test
    @DisplayName("책 태그 삭제 - 책 없음")
    void deleteTagByBookBookNotFoundTest() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> bookTagService.deleteTagByBook(1L, 1L))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    @DisplayName("책 태그 삭제 - 태그 없음")
    void deleteTagByBookTagNotFoundTest() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> bookTagService.deleteTagByBook(1L, 1L))
                .isInstanceOf(TagNotFoundException.class);
    }

    @Test
    @DisplayName("책 태그 삭제 - 매핑 없음")
    void deleteTagByBookNotFoundTest() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(tagRepository.existsById(1L)).thenReturn(true);
        when(bookTagRepository.findById(any(BookTagId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookTagService.deleteTagByBook(1L, 1L))
                .isInstanceOf(BookTagNotFoundException.class);
    }
}
