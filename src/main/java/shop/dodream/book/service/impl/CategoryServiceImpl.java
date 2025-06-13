package shop.dodream.book.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.Category;
import shop.dodream.book.exception.CategoryIdNotFoundException;
import shop.dodream.book.exception.CategoryNameIsNullException;
import shop.dodream.book.exception.CategoryParentIdNotFoundException;
import shop.dodream.book.repository.BookCategoryRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.CategoryRepository;
import shop.dodream.book.service.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Override @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // 관리자만 가능하게 할지????
        if (request.getCategoryName() == null || request.getCategoryName().isEmpty()) {
            throw new CategoryNameIsNullException("생성하고자 하는 카테고리 이름이 비어 있습니다.");
        }
        Category category = new Category();
        applyCategoryRequestToEntity(category, request);
        Category savedCategory = categoryRepository.save(category);

//        if (request.getParentId() == null) {
//            Category defaultChild = new Category();
//            defaultChild.setCategoryName("기본 하위 카테고리");
//            defaultChild.setDepth(2);
//            defaultChild.setParent(category);
//            categoryRepository.save(defaultChild);
//            savedCategory.addChild(defaultChild);
//        }
        return new CategoryTreeResponse(savedCategory);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryIdNotFoundException(categoryId, " 라는 카테고리 아이디를 찾을 수 없습니다."));
        return new CategoryResponse(category);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getCategoriesChildren(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryIdNotFoundException(categoryId, " 라는 카테고리 아이디를 찾을 수 없습니다."));

        List<Category> result = new ArrayList<>();
        result.add(category);

        return result.stream()
                .map(CategoryTreeResponse::new)
                .collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getCategoriesRelated(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryIdNotFoundException(categoryId, " 라는 카테고리 아이디를 찾을 수 없습니다."));

        while (category.getParent() != null) {
            category = category.getParent();
        }

        return List.of(new CategoryTreeResponse(category));
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesByBook(Long bookId) {
        List<Category> categories = bookCategoryRepository.findCategoriesByBook(bookId);
        return categories.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<BookResponse> getBooksByCategory(Long categoryId) {
        List<Book> books = bookCategoryRepository.findBooksByCategory(categoryId);
        return books.stream()
                .map(BookResponse::new)
                .collect(Collectors.toList());
    }

    @Override @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryIdNotFoundException(categoryId, " 라는 카테고리 아이디를 찾을 수 없습니다."));
        applyCategoryRequestToEntity(category, request);
        //TODO 자기 자신을 부모 카테고리로 지정할 수 없게 예외 설정


        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(savedCategory);
    }

    @Override @Transactional
    public void deleteCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryIdNotFoundException(categoryId, " 라는 카테고리 아이디를 찾을 수 없습니다."));
        categoryRepository.delete(category);
    }

    // create, update 공통부분
    private void applyCategoryRequestToEntity(Category category, CategoryRequest request) {
        category.setCategoryName(request.getCategoryName());
        if (request.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CategoryParentIdNotFoundException(request.getParentId(), " 라는 부모 카테고리를 찾을 수 없습니다."));
            category.setDepth(parentCategory.getDepth() + 1);
            category.setParent(parentCategory);
            parentCategory.addChild(category);
        } else {
            category.setDepth(1);
            category.setParent(null);
        }
    }
}
