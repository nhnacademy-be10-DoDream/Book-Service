package shop.dodream.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookResponse;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.Category;
import shop.dodream.book.exception.CategoryIdNotFoundException;
import shop.dodream.book.exception.CategoryNameIsNullException;
import shop.dodream.book.exception.CategoryParentIdNotFoundException;
import shop.dodream.book.repository.BookCategoryRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.CategoryRepository;
import shop.dodream.service.CategoryService;

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
        return new CategoryResponse(savedCategory);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
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
        category.setDepth(request.getDepth());
//        if (request.getCategory() != null) {
//            Category parentCategory = categoryRepository.findByCategory(request.getCategory())
//                    .orElseThrow(() -> new CategoryParentIdNotFoundException(request.getCategory(), " 라는 부모 카테고리를 찾을 수 없습니다."));
//            category.setCategory(parentCategory);
//        }
    }
}
