package shop.dodream.book.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.entity.Category;
import shop.dodream.book.exception.CategoryDepthNotFoundException;
import shop.dodream.book.exception.CategoryNotDeleteWithChildren;
import shop.dodream.book.exception.CategoryNotFoundException;
import shop.dodream.book.exception.InvalidParentCategoryException;
import shop.dodream.book.repository.CategoryRepository;
import shop.dodream.book.service.impl.CategoryServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    @DisplayName("카테고리 생성 - 부모 없는 경우")
    void createCategory_noParent() {
        CategoryRequest request = new CategoryRequest("신규카테고리", 0);

        Category savedCategory = new Category(1L, "신규카테고리", 1L, null, new ArrayList<>());

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponse response = categoryService.createCategory(request);

        assertThat(response).isInstanceOf(CategoryTreeResponse.class);
        assertThat(response.getCategoryId()).isEqualTo(1L);
        assertThat(response.getCategoryName()).isEqualTo("신규카테고리");
    }

    @Test
    @DisplayName("카테고리 생성 - 부모 있는 경우")
    void createCategory_withParent() {
        CategoryRequest request = new CategoryRequest("자식카테고리", 1L);

        Category parentCategory = new Category(1L, "부모카테고리", 1L, null, new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));

        Category savedCategory = new Category(2L, "자식카테고리", 2L, parentCategory, new ArrayList<>());

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponse response = categoryService.createCategory(request);

        assertThat(response.getCategoryId()).isEqualTo(2L);
        assertThat(response.getCategoryName()).isEqualTo("자식카테고리");
    }

    @Test
    @DisplayName("카테고리 전체 조회")
    void getCategories_success() {
        List<Category> categories = List.of(
                new Category(1L, "테스트카테고리1", 1L, null, new ArrayList<>()),
                new Category(2L, "테스트카테고리2", 1L, null, new ArrayList<>())
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryResponse> responses = categoryService.getCategories();

        assertThat(responses).hasSize(categories.size());
        assertThat(responses).extracting("categoryName")
                .containsExactlyInAnyOrder("테스트카테고리1", "테스트카테고리2");
    }

    @Test
    @DisplayName("카테고리 단건 조회")
    void getCategory_success() {
        Category category = new Category(1L, "테스트카테고리", 1L, null, new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.getCategory(1L);

        assertThat(response).isNotNull();
        assertThat(response.getCategoryName()).isEqualTo("테스트카테고리");
    }

    @Test
    @DisplayName("카테고리 단건 조회 - 없는 경우 예외")
    void getCategory_notFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategory(999L))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("getCategoriesChildren: 주어진 카테고리 ID에 해당하는 노드와 자식 노드 반환")
    void getCategoriesChildren_success() {
        List<CategoryFlatProjection> flats = createFlatCategories();
        when(categoryRepository.findAllFlat()).thenReturn(flats);

        List<CategoryTreeResponse> result = categoryService.getCategoriesChildren(2L);

        assertThat(result).isNotEmpty();
        CategoryTreeResponse node = result.get(0);
        assertThat(node.getCategoryId()).isEqualTo(2L);
        assertThat(node.getChildren()).extracting("categoryId")
                .containsExactly(4L);
    }

    @Test
    @DisplayName("getCategoriesChildren: 없는 카테고리 ID 입력시 예외 발생")
    void getCategoriesChildren_notFound() {
        when(categoryRepository.findAllFlat()).thenReturn(createFlatCategories());

        assertThatThrownBy(() -> categoryService.getCategoriesChildren(999L))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("getCategoriesRelated: 주어진 카테고리 ID로 최상위 루트 노드 반환")
    void getCategoriesRelated_success() {
        List<CategoryFlatProjection> flats = createFlatCategories();
        when(categoryRepository.findAllFlat()).thenReturn(flats);

        List<CategoryTreeResponse> result = categoryService.getCategoriesRelated(4L);

        assertThat(result).isNotEmpty();
        CategoryTreeResponse root = result.get(0);
        assertThat(root.getCategoryId()).isEqualTo(1L);
        assertThat(root.getChildren()).isNotEmpty();
    }

    @Test
    @DisplayName("getCategoriesRelated: 없는 카테고리 ID 입력시 예외 발생")
    void getCategoriesRelated_notFound() {
        when(categoryRepository.findAllFlat()).thenReturn(createFlatCategories());

        assertThatThrownBy(() -> categoryService.getCategoriesRelated(999L))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("getCategoriesPath: 주어진 카테고리 ID부터 루트까지 경로 반환")
    void getCategoriesPath_success() {
        List<CategoryFlatProjection> flats = createFlatCategories();
        when(categoryRepository.findAllFlat()).thenReturn(flats);

        List<CategoryFlatProjection> path = categoryService.getCategoriesPath(4L);

        assertThat(path).extracting(CategoryFlatProjection::getCategoryId)
                .containsExactly(1L, 2L, 4L);
    }

    @Test
    @DisplayName("getCategoriesPath: 없는 카테고리 ID 입력시 예외 발생")
    void getCategoriesPath_notFound() {
        when(categoryRepository.findAllFlat()).thenReturn(createFlatCategories());

        assertThatThrownBy(() -> categoryService.getCategoriesPath(999L))
                .isInstanceOf(CategoryNotFoundException.class);
    }



    @Test
    @DisplayName("카테고리 수정 성공")
    void updateCategory_success() {
        CategoryRequest request = new CategoryRequest("수정된카테고리", 1L);

        Category category = new Category(1L, "원래카테고리", 1L, null, new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse response = categoryService.updateCategory(1L, request);

        assertThat(response.getCategoryId()).isEqualTo(1L);
        assertThat(response.getCategoryName()).isEqualTo("수정된카테고리");
    }



    @Test
    @DisplayName("카테고리 수정 실패 - 순환 오류(부모가 자식 노드인 경우)")
    void updateCategory_invalidParent_cyclicLoop() {
        CategoryRequest request = new CategoryRequest();
        request.setCategoryName("무관");
        request.setDepth(2L);
        request.setParentId(2L);

        Category category = new Category(1L, "원래카테고리", 1L, null, new ArrayList<>());

        Category child = new Category(2L, "자식카테고리", 2L, category, new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(child));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThatThrownBy(() -> categoryService.updateCategory(1L, request))
                .isInstanceOf(InvalidParentCategoryException.class);
    }

    @Test
    @DisplayName("카테고리 수정 - 부모가 존재하는 경우")
    void updateCategory_withValidParent() {
        CategoryRequest request = new CategoryRequest();
        request.setCategoryName("프로그래밍 언어");
        request.setParentId(2L);  // 부모 존재

        Category parent = new Category(2L, "IT", 1L, null, new ArrayList<>());
        Category category = new Category(1L, "기존 카테고리", 1L, null, new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(parent));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        CategoryResponse result = categoryService.updateCategory(1L, request);

        assertThat(result.getParentId()).isEqualTo(2L);
        assertThat(result.getDepth()).isEqualTo(2L);
        assertThat(result.getCategoryName()).isEqualTo("프로그래밍 언어");
    }

    @Test
    @DisplayName("카테고리 수정 실패 - 부모가 존재하지 않음")
    void updateCategory_withInvalidParentId() {
        CategoryRequest request = new CategoryRequest();
        request.setCategoryName("자료구조");
        request.setParentId(99L); // 존재하지 않는 부모 ID

        Category category = new Category(1L, "기존", 1L, null, new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(1L, request))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("카테고리 수정 - 최상위 카테고리로 변경")
    void updateCategory_withNoParent() {
        CategoryRequest request = new CategoryRequest();
        request.setCategoryName("문학");
        request.setParentId(null);

        Category category = new Category(1L, "기존", 2L, new Category(10L, "부모", 1L, null, new ArrayList<>()), new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        CategoryResponse result = categoryService.updateCategory(1L, request);

        assertThat(result.getParentId()).isNull();
        assertThat(result.getDepth()).isEqualTo(1L);
        assertThat(result.getCategoryName()).isEqualTo("문학");
    }






    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategory_success() {
        Category category = new Category(1L, "삭제가능카테고리", 1L, null, new ArrayList<>());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    @DisplayName("카테고리 삭제 실패 - 자식이 있음")
    void deleteCategory_fail_hasChildren() {
        Category child = new Category(2L, "자식카테고리", 2L, null, new ArrayList<>());

        List<Category> children = new ArrayList<>();
        children.add(child);

        Category parentCategory = new Category(1L, "부모카테고리", 1L, null, children);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));

        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(CategoryNotDeleteWithChildren.class);
    }

    @Test
    @DisplayName("카테고리 깊이로 조회 - 없을 때 예외")
    void getCategoriesDepth_notFound() {
        when(categoryRepository.findByDepth(99L)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> categoryService.getCategoriesDepth(99L))
                .isInstanceOf(CategoryDepthNotFoundException.class);
    }

    @Test
    @DisplayName("카테고리 깊이로 조회 - 성공")
    void getCategoriesDepth_success() {
        Category category = new Category(1L, "깊이카테고리", 2L, null, new ArrayList<>());

        when(categoryRepository.findByDepth(2L)).thenReturn(Arrays.asList(category));

        List<CategoryResponse> responses = categoryService.getCategoriesDepth(2L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getCategoryName()).isEqualTo("깊이카테고리");
    }


    private List<CategoryFlatProjection> createFlatCategories() {
        return List.of(
                new CategoryFlatProjection() {
                    public Long getCategoryId() { return 1L; }
                    public String getCategoryName() { return "root"; }
                    public Long getDepth() { return 1L; }
                    public Long getParentId() { return null; }
                },
                new CategoryFlatProjection() {
                    public Long getCategoryId() { return 2L; }
                    public String getCategoryName() { return "child1"; }
                    public Long getDepth() { return 2L; }
                    public Long getParentId() { return 1L; }
                },
                new CategoryFlatProjection() {
                    public Long getCategoryId() { return 3L; }
                    public String getCategoryName() { return "child2"; }
                    public Long getDepth() { return 2L; }
                    public Long getParentId() { return 1L; }
                },
                new CategoryFlatProjection() {
                    public Long getCategoryId() { return 4L; }
                    public String getCategoryName() { return "grandchild"; }
                    public Long getDepth() { return 3L; }
                    public Long getParentId() { return 2L; }
                }
        );
    }
}
