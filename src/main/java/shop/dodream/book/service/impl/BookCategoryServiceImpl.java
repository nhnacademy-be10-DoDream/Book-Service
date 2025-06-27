package shop.dodream.book.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.BookWithCategoriesResponse;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.IdsListRequest;
import shop.dodream.book.dto.projection.BookListResponseRecord;
import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.dto.projection.CategoryWithParentProjection;
import shop.dodream.book.entity.Book;
import shop.dodream.book.entity.BookCategory;
import shop.dodream.book.entity.BookCategoryId;
import shop.dodream.book.entity.Category;
import shop.dodream.book.exception.*;
import shop.dodream.book.repository.BookCategoryRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.CategoryRepository;
import shop.dodream.book.service.BookCategoryService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override @Transactional
    public BookWithCategoriesResponse registerCategory(Long bookId, IdsListRequest categoryIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        Set<Long> requestCategoryIds = new HashSet<>(categoryIds.getIds()); // 새로 등록할 카테고리
        Set<Long> existingCategoryIds = bookCategoryRepository.findCategoryIdsByBookId(bookId); // 이미 책에 등록된 카테고리

        validateDuplicate(requestCategoryIds, existingCategoryIds); // 중복 카테고리 검사

        int totalCount = existingCategoryIds.size() + categoryIds.getIds().size(); // 카테고리 10 Over 예외
        if (totalCount > 10) {
            throw new CategoryRegisterOverException();
        }

        // 모든 조상 카테고리 정보 -> categoryMap에 추가
        Map<Long, CategoryWithParentProjection> categoryMap = fetchCategoryHierarchy(getAllIds(requestCategoryIds, existingCategoryIds));

        // categoryMap에 있는 Id가 전부 존재하는지 검증
        validateCategoryExistence(requestCategoryIds, categoryMap);

        // 부모 카테고리와 자식 카테고리 동시 등록 불가, 등록할때 부모,자식이 이미 등록되어있을 경우
        validateParentChildConflict(requestCategoryIds, existingCategoryIds, categoryMap);

        List<CategoryWithParentProjection> initialCategories = requestCategoryIds.stream()
                .map(categoryMap::get)
                .toList();

        // 등록
        List<BookCategory> entities = createBookCategoryEntities(book, initialCategories);
        entities.forEach(entityManager::persist);

        List<CategoryResponse> categoryResponses = initialCategories.stream()
                .map(dto -> new CategoryResponse(dto.id(), dto.categoryName(), dto.depth(), dto.parentId()))
                .toList();

        return new BookWithCategoriesResponse(book.getId(), categoryResponses);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getCategoriesByBookId(Long bookId){
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        Set<Long> categoryIds = bookCategoryRepository.findCategoryIdsByBookId(bookId);

        List<CategoryFlatProjection> flatCategories = categoryRepository.findAllFlat();

        Map<Long, CategoryTreeResponse> map = new HashMap<>();
        for (CategoryFlatProjection c : flatCategories) {
            map.put(c.getCategoryId(), new CategoryTreeResponse(
                    c.getCategoryId(), c.getCategoryName(), c.getDepth(), c.getParentId()
            ));
        }

        Set<CategoryTreeResponse> result = new HashSet<>();
        for (Long id : categoryIds) {
            CategoryTreeResponse leaf = map.get(id);
            if (leaf == null) continue;
            CategoryTreeResponse current = copyNode(leaf);

            while (leaf.getParentId() != null && !leaf.getParentId().equals(leaf.getCategoryId())) {
                CategoryTreeResponse parentInMap = map.get(leaf.getParentId());
                if (parentInMap == null) break;

                CategoryTreeResponse parentCopy = copyNode(parentInMap);
                parentCopy.getChildren().add(current);
                current = parentCopy;
                leaf = parentInMap;
            }

            result.add(current);
        }

        return new ArrayList<>(result);
    }

    @Override @Transactional(readOnly = true)
    public Page<BookListResponseRecord> getBooksByCategoryId(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        return bookCategoryRepository.findBookListByCategoryId(categoryId, pageable);
    }

    @Override @Transactional
    public Long updateCategoryByBook(Long bookId, Long categoryId, Long newCategoryId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        Category category = categoryRepository.findById(newCategoryId)
                .orElseThrow(() -> new CategoryNotFoundException(newCategoryId));

        BookCategory bookCategory = bookCategoryRepository.findExistingCategory(bookId, categoryId)
                .orElseThrow(() -> new BookCategoryNotFoundException(bookId, categoryId));

        if (bookCategoryRepository.existsByBookIdAndCategoryId(bookId, newCategoryId)) {
            throw new BookCategoryDuplicateException(bookId, newCategoryId);
        }

        bookCategoryRepository.delete(bookCategory);
        bookCategoryRepository.save(new BookCategory(book, category));

        return newCategoryId;
    }

    @Override @Transactional
    public void deleteCategoriesByBook(Long bookId, IdsListRequest categoryIds) {
        if (categoryIds == null || categoryIds.getIds().isEmpty()) {
            return;
        }
        List<Long> existingCategoryIds = bookCategoryRepository.findExistingCategoryIds(bookId, categoryIds.getIds());
        if (existingCategoryIds.isEmpty()) {
            throw new BookCategoriesNotFoundException(bookId, categoryIds.getIds());
        }
        bookCategoryRepository.deleteByBookIdAndCategoryIds(bookId, categoryIds.getIds());
    }

    private CategoryTreeResponse copyNode(CategoryTreeResponse response) {
        return new CategoryTreeResponse(
                response.getCategoryId(),
                response.getCategoryName(),
                response.getDepth(),
                response.getParentId()
        );
    }

    private void validateDuplicate(Set<Long> requestCategoryIds, Set<Long> existingCategoryIds){
        Set<Long> intersection = new HashSet<>(existingCategoryIds);
        intersection.retainAll(requestCategoryIds);
        if (!intersection.isEmpty()) {
            throw new CategoryAlreadyRegisteredException(intersection);
        }
    }

    private Map<Long, CategoryWithParentProjection> fetchCategoryHierarchy(Set<Long> allCategoryIds) {
        Map<Long, CategoryWithParentProjection> categoryMap = new HashMap<>();
        Queue<Long> queue = new LinkedList<>(allCategoryIds);

        while (!queue.isEmpty()) {
            List<Long> batch = new ArrayList<>();
            while (!queue.isEmpty()) {
                Long id = queue.poll();
                if (!categoryMap.containsKey(id)) {
                    batch.add(id);
                }
            }

            if (batch.isEmpty()) break;

            List<CategoryWithParentProjection> batchCategories = categoryRepository.findAllByIdsWithParent(batch);
            for (CategoryWithParentProjection cat : batchCategories) {
                categoryMap.put(cat.id(), cat);
                if (cat.parentId() != null && !categoryMap.containsKey(cat.parentId())) {
                    queue.add(cat.parentId());
                }
            }
        }

        return categoryMap;
    }


    private Set<Long> getAllIds(Set<Long> requestCategoryIds, Set<Long> existingCategoryIds) {
        Set<Long> allIds = new HashSet<>();
        allIds.addAll(requestCategoryIds);
        allIds.addAll(existingCategoryIds);
        return allIds;
    }

    private void validateCategoryExistence(Set<Long> requestCategoryIds, Map<Long, CategoryWithParentProjection> categoryMap) {
        List<Long> existingIds = requestCategoryIds.stream()
                .filter(id -> categoryMap.get(id) == null)
                .toList();

        if (!existingIds.isEmpty()) {
            throw new CategoryNotFoundException();
        }
    }

    private void validateParentChildConflict(Set<Long> requestCategoryIds, Set<Long> existingCategoryIds, Map<Long, CategoryWithParentProjection> categoryMap) {
        for (Long requestId : requestCategoryIds) {
            checkParentConflict(requestId, requestCategoryIds, existingCategoryIds, categoryMap);
        }

        for (Long existingId : existingCategoryIds) {
            checkChildConflict(existingId, requestCategoryIds, categoryMap);
        }
    }

    private void checkParentConflict(Long id, Set<Long> requestCategoryIds, Set<Long> existingCategoryIds, Map<Long, CategoryWithParentProjection> categoryMap) {
        Long parentId = categoryMap.get(id).parentId();
        while (parentId != null) {
            if (requestCategoryIds.contains(parentId)) {
                throw new CategoryParentChildConflictException(id, parentId);
            }
            if (existingCategoryIds.contains(parentId)) {
                throw new CategoryParentChildConflictException(id, parentId);
            }
            parentId = categoryMap.get(parentId) != null ? categoryMap.get(parentId).parentId() : null;
        }
    }

    private void checkChildConflict(Long existingId, Set<Long> requestCategoryIds, Map<Long, CategoryWithParentProjection> categoryMap) {
        Long parentId = categoryMap.get(existingId) != null ? categoryMap.get(existingId).parentId() : null;
        while (parentId != null) {
            if (requestCategoryIds.contains(parentId)) {
                throw new CategoryParentChildConflictException(existingId, parentId);
            }
            parentId = categoryMap.get(parentId) != null ? categoryMap.get(parentId).parentId() : null;
        }
    }

    private List<BookCategory> createBookCategoryEntities(Book book, List<CategoryWithParentProjection> categories) {
        return categories.stream()
                .map(dto -> {
                    Category categoryRef = entityManager.getReference(Category.class, dto.id());
                    return new BookCategory(new BookCategoryId(book.getId(), dto.id()), book, categoryRef);
                }).toList();
    }
}
