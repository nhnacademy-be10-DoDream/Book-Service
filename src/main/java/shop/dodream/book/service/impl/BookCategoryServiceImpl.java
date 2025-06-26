package shop.dodream.book.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.*;
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


        //TODO 빈 리스트 체크 필요

        Set<Long> requestCategoryIds = new HashSet<>(categoryIds.getIds()); // 새로 등록할 카테고리
        Set<Long> existingCategoryIds = bookCategoryRepository.findCategoryIdsByBookId(bookId); // 이미 책에 등록된 카테고리

        Set<Long> intersection = new HashSet<>(existingCategoryIds); // 중복 카테고리 검사
        intersection.retainAll(requestCategoryIds);
        if (!intersection.isEmpty()) {
            throw new CategoryAlreadyRegisteredException(intersection);
        }

        int totalCount = existingCategoryIds.size() + categoryIds.getIds().size(); // 카테고리 10 Over 예외
        if (totalCount > 10) {
            throw new CategoryRegisterOverException();
        }

        Set<Long> allIdsToFetch = new HashSet<>();
        allIdsToFetch.addAll(requestCategoryIds);
        allIdsToFetch.addAll(existingCategoryIds);

        Map<Long, CategoryWithParentProjection> categoryMap = new HashMap<>();
        Queue<Long> queue = new LinkedList<>(allIdsToFetch);

        while (!queue.isEmpty()) { // 모든 조상 카테고리 정보 -> categoryMap에 추가
            List<Long> batch = new ArrayList<>();
            while (!queue.isEmpty()) {
                Long id = queue.poll();
                if (!categoryMap.containsKey(id)) {
                    batch.add(id);
                }
            }

            if (batch.isEmpty()) break;

            List<CategoryWithParentProjection> batchCategories =
                    categoryRepository.findAllByIdsWithParent(batch);

            for (CategoryWithParentProjection cat : batchCategories) {
                categoryMap.put(cat.id(), cat);
                if (cat.parentId() != null && !categoryMap.containsKey(cat.parentId())) {
                    queue.add(cat.parentId());
                }
            }
        }

        // categoryMap에 있는 Id가 전부 존재하는지 검증
        List<CategoryWithParentProjection> initialCategories = requestCategoryIds.stream()
                .map(categoryMap::get)
                .filter(Objects::nonNull)
                .toList();
        if (initialCategories.size() != requestCategoryIds.size()) {
            throw new CategoryNotFoundException();
        }

        for (CategoryWithParentProjection requestedCat : initialCategories) { // 부모 카테고리와 자식 카테고리 동시 등록 불가
            Long currentCatId = requestedCat.id();
            Long parentId = requestedCat.parentId();

            while (parentId != null) {
                if (requestCategoryIds.contains(parentId)) {
                    throw new CategoryParentChildConflictException(currentCatId, parentId);
                }
                CategoryWithParentProjection parentProjection = categoryMap.get(parentId);
                parentId = (parentProjection != null) ? parentProjection.parentId() : null;
            }
        }

        // 등록할때 부모가 이미 등록되어있을 경우
        for (CategoryWithParentProjection category : initialCategories) {
            Long current = category.parentId();
            while (current != null) {
                if (existingCategoryIds.contains(current)) {
                    throw new CategoryParentChildConflictException(category.id(), current);
                }
                CategoryWithParentProjection parent = categoryMap.get(current);
                current = (parent != null) ? parent.parentId() : null;
            }
        }

        // 등록할때 자식이 이미 등록되어있을 경우
        for (Long existingId : existingCategoryIds) {
            Long current = categoryMap.containsKey(existingId)
                    ? categoryMap.get(existingId).parentId() : null;
            while (current != null) {
                if (requestCategoryIds.contains(current)) {
                    throw new CategoryParentChildConflictException(existingId, current);
                }
                CategoryWithParentProjection parent = categoryMap.get(current);
                current = (parent != null) ? parent.parentId() : null;
            }
        }

        // 등록
        List<BookCategory> entities = initialCategories.stream()
                .map(dto -> {
                    Category categoryRef = entityManager.getReference(Category.class, dto.id());
                    return new BookCategory(new BookCategoryId(book.getId(), dto.id()), book, categoryRef);
                }).toList();

        for (BookCategory entity : entities) {
            entityManager.persist(entity);
        }

        List<CategoryResponse> categoryResponses = initialCategories.stream()
                .map(dto -> new CategoryResponse(dto.id(), dto.categoryName(), dto.depth(), dto.parentId()))
                .toList();

        return new BookWithCategoriesResponse(book.getId(), categoryResponses);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getCategoriesByBookId(Long bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        Set<Long> categoryIds = bookCategoryRepository.findCategoryIdsByBookId(bookId);

        //TODO 빈 리스트 체크 필요

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

            CategoryTreeResponse node = copyNode(leaf);
            CategoryTreeResponse current = node;

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
    public List<BookListResponse> getBooksByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        List<Long> bookIds = bookCategoryRepository.findBookIdsByCategoryId(categoryId);
        List<Book> books = bookRepository.findAllById(bookIds);

        return books.stream()
                .map(book -> new BookListResponse(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getRegularPrice(),
                        book.getSalePrice(),
                        book.getBookUrl()
                ))
                .toList();
    }

    @Override @Transactional
    public Long updateCategoryByBook(Long bookId, Long categoryId, Long newCategoryId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        Category category = categoryRepository.findById(newCategoryId)
                .orElseThrow(() -> new CategoryNotFoundException(newCategoryId));

        BookCategory bookCategory = bookCategoryRepository.findExistingCategoryId(bookId, categoryId)
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
}
