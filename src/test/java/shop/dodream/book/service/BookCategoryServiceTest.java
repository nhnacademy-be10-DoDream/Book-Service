package shop.dodream.book.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import shop.dodream.book.dto.*;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.dto.projection.CategoryWithParentProjection;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.BookStatus;
import shop.dodream.book.entity.Category;
import shop.dodream.book.repository.BookCategoryRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.CategoryRepository;
import shop.dodream.book.service.impl.BookCategoryServiceImpl;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookCategoryServiceTest {

    @Mock
    private EntityManager entityManager;
    private BookRepository bookRepository;
    private BookCategoryRepository bookCategoryRepository;
    private CategoryRepository categoryRepository;
    private BookCategoryServiceImpl bookCategoryService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookCategoryRepository = mock(BookCategoryRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        entityManager = mock(EntityManager.class);
        bookCategoryService = new BookCategoryServiceImpl(
                bookCategoryRepository, bookRepository, categoryRepository);
        ReflectionTestUtils.setField(bookCategoryService, "entityManager", entityManager);

        // entityManager.getReference() 동작 모킹
        when(entityManager.getReference(eq(Category.class), anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(1);
            return new Category(id, "테스트 카테고리", 1L, null);
        });

    }

    @Test
    @DisplayName("책에 카테고리 등록 성공")
    void registerCategory_success() {
        Long bookId = 1L;
        List<Long> ids = List.of(10L);
        IdsListRequest request = new IdsListRequest(ids);

        Book book = new Book(
                "테스트 제목",
                "테스트 설명",
                "저자",
                "출판사",
                LocalDate.now(),
                "1234567890",
                20000L,
                BookStatus.SELL,
                15000L,
                true,
                0L,
                10L
        );
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookCategoryRepository.findCategoryIdsByBookId(bookId)).thenReturn(new HashSet<>());

        CategoryWithParentProjection projection =
                new CategoryWithParentProjection(10L, "카테고리", 1L, null, null);

        when(categoryRepository.findAllByIdsWithParent(eq(ids))).thenReturn(List.of(projection));
        Category category = new Category(10L);
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        BookWithCategoriesResponse response = bookCategoryService.registerCategory(bookId, request);

        assertThat(response).isNotNull();
        assertThat(response.getCategories()).hasSize(1);

        verify(entityManager, atLeastOnce()).persist(any(BookCategory.class));

    }




    @Test
    @DisplayName("책의 카테고리 계층 조회")
    void getCategoriesByBookId_success() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookCategoryRepository.findCategoryIdsByBookId(bookId)).thenReturn(Set.of(10L));

        CategoryFlatProjection cat = mock(CategoryFlatProjection.class);
        when(cat.getCategoryId()).thenReturn(10L);
        when(cat.getCategoryName()).thenReturn("카테고리");
        when(cat.getDepth()).thenReturn(1L);
        when(cat.getParentId()).thenReturn(null);
        when(categoryRepository.findAllFlat()).thenReturn(List.of(cat));

        List<CategoryTreeResponse> responses = bookCategoryService.getCategoriesByBookId(bookId);
        assertThat(responses).hasSize(1);
    }

    @Test
    @DisplayName("책의 평면 카테고리 조회")
    void getFlatCategoriesByBookId_success() {
        Long bookId = 1L;

        Book book = new Book(
                "테스트 제목",
                "테스트 설명",
                "저자",
                "출판사",
                LocalDate.now(),
                "1234567890",
                20000L,
                BookStatus.SELL,
                15000L,
                true,
                0L,
                10L
        );

        Category category = new Category(1L, "국내도서", 1L, null, new ArrayList<>());

        BookCategory bookCategory = new BookCategory(book, category);

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookCategoryRepository.findByBookId(bookId)).thenReturn(List.of(bookCategory));

        List<CategoryResponse> responses = bookCategoryService.getFlatCategoriesByBookId(bookId);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getCategoryName()).isEqualTo("국내도서");
    }


    @Test
    @DisplayName("카테고리별 책 조회")
    void getBooksByCategoryId_success() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        CategoryFlatProjection projection = mock(CategoryFlatProjection.class);
        when(projection.getCategoryId()).thenReturn(1L);
        when(projection.getParentId()).thenReturn(null);
        when(categoryRepository.findAllFlat()).thenReturn(List.of(projection));

        Page<BookListResponseRecord> page = new PageImpl<>(List.of(mock(BookListResponseRecord.class)));
        when(bookCategoryRepository.findBookListByCategoryIds(anySet(), eq(pageable))).thenReturn(page);

        Page<BookListResponseRecord> result = bookCategoryService.getBooksByCategoryId(categoryId, pageable);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("책의 카테고리 수정")
    void updateCategoryByBook_success() {
        Long bookId = 1L;
        Long oldCatId = 2L;
        Long newCatId = 3L;

        Book book = mock(Book.class);
        Category category = new Category();
        BookCategory bookCategory = new BookCategory();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(categoryRepository.findById(newCatId)).thenReturn(Optional.of(category));
        when(bookCategoryRepository.findExistingCategory(bookId, oldCatId)).thenReturn(Optional.of(bookCategory));
        when(bookCategoryRepository.existsByBookIdAndCategoryId(bookId, newCatId)).thenReturn(false);

        BookDocument document = new BookDocument();
        document.setCategoryIds(new ArrayList<>(List.of(oldCatId)));
        document.setCategoryNames(new ArrayList<>(List.of("oldCategory")));

        bookCategoryService.updateCategoryByBook(bookId, oldCatId, newCatId);

        verify(bookCategoryRepository).delete(bookCategory);
        verify(bookCategoryRepository).save(any(BookCategory.class));
    }



    @Test
    @DisplayName("책의 카테고리 삭제")
    void deleteCategoriesByBook_success() {
        Long bookId = 1L;
        List<Long> ids = List.of(5L);
        IdsListRequest request = new IdsListRequest(ids);

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookCategoryRepository.findExistingCategoryIds(bookId, ids)).thenReturn(ids);

        BookDocument mockDocument = new BookDocument();
        mockDocument.setCategoryIds(new ArrayList<>());
        mockDocument.setCategoryNames(new ArrayList<>());

        bookCategoryService.deleteCategoriesByBook(bookId, request);

        verify(bookCategoryRepository).deleteByBookIdAndCategoryIds(bookId, ids);
    }

}
