package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dodream.book.dto.CategoryRequest;
import shop.dodream.book.dto.CategoryResponse;
import shop.dodream.book.dto.CategoryTreeResponse;
import shop.dodream.book.dto.projection.CategoryFlatProjection;
import shop.dodream.book.entity.Category;
import shop.dodream.book.exception.CategoryDepthNotFoundException;
import shop.dodream.book.exception.CategoryNotDeleteWithChildren;
import shop.dodream.book.exception.CategoryNotFoundException;
import shop.dodream.book.exception.InvalidParentCategoryException;
import shop.dodream.book.repository.BookCategoryRepository;
import shop.dodream.book.repository.BookRepository;
import shop.dodream.book.repository.CategoryRepository;
import shop.dodream.book.service.CategoryService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Override @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        applyCategoryRequestToEntity(category, request);
        Category savedCategory = categoryRepository.save(category);

        return new CategoryTreeResponse(savedCategory);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponse::new)
                .toList();
    }

    @Override @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return new CategoryResponse(category);
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getCategoriesChildren(Long categoryId) {
        // 플랫 구조로 조회
        List<CategoryFlatProjection> flatCategories = categoryRepository.findAllFlat();

        // 플랫 구조 -> 트리 구조
        List<CategoryTreeResponse> tree = buildCategoryTree(flatCategories);

        // categoryId에 맞는 노드 반환
        CategoryTreeResponse targetNode = findNodeById(tree, categoryId);
        if (targetNode == null) { // 해당 노드가 없을때
            throw new CategoryNotFoundException(categoryId);
        }

        List<CategoryTreeResponse> result = new ArrayList<>();
        result.add(targetNode);
        return result;


    }

    @Override @Transactional(readOnly = true)
    public List<CategoryTreeResponse> getCategoriesRelated(Long categoryId) {
        // 플랫 구조로 조회
        List<CategoryFlatProjection> flatCategories = categoryRepository.findAllFlat();

        // 플랫 구조 -> 트리 구조
        List<CategoryTreeResponse> tree = buildCategoryTree(flatCategories);

        // categoryId에 맞는 노드 반환
        CategoryTreeResponse targetNode = findNodeById(tree, categoryId);
        if (targetNode == null) { // 해당 노드가 없을때
            throw new CategoryNotFoundException(categoryId);
        }

        // 최상위 노드까지
        CategoryTreeResponse rootNode = targetNode;
        while (Objects.requireNonNull(rootNode).getParentId() != null) {
            rootNode = findNodeById(tree, rootNode.getParentId());
        }

        List<CategoryTreeResponse> result = new ArrayList<>();
        result.add(rootNode);
        return result;
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryFlatProjection> getCategoriesPath(Long categoryId) {
        List<CategoryFlatProjection> flatCategories = categoryRepository.findAllFlat();
        Map<Long, CategoryFlatProjection> idToCategory = flatCategories.stream()
                .collect(Collectors.toMap(CategoryFlatProjection::getCategoryId, c -> c));

        List<CategoryFlatProjection> path = new ArrayList<>();
        CategoryFlatProjection current = idToCategory.get(categoryId);
        if (current == null) {
            throw new CategoryNotFoundException(categoryId);
        }

        while (current != null) {
            path.add(current);
            if (current.getParentId() == null) break;
            current = idToCategory.get(current.getParentId());
        }

        Collections.reverse(path);

        return path;
    }

    @Override @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesDepth(Long depth) {
        List<Category> categories = categoryRepository.findByDepth(depth);
        if (categories.isEmpty()) {
            throw new CategoryDepthNotFoundException(depth);
        }
        return categories.stream()
                .map(CategoryResponse::new)
                .toList();

    }

    @Override @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        if (request.getParentId() != null && categoryId.equals(request.getParentId())) {
            throw new InvalidParentCategoryException();
        }

        if (request.getParentId() != null && isCyclicParent(category, request.getParentId())) {
            throw new InvalidParentCategoryException("순환 참조 오류 발생");
        }

        applyCategoryRequestToEntity(category, request);
        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(savedCategory);
    }

    @Override @Transactional
    public void deleteCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        if (!category.getChildren().isEmpty()) {
            throw new CategoryNotDeleteWithChildren(categoryId);
        }
        categoryRepository.delete(category);
    }

    // create, update 공통부분
    private void applyCategoryRequestToEntity(Category category, CategoryRequest request) {
        category.setCategoryName(request.getCategoryName());
        if (request.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CategoryNotFoundException(category.getId(), request.getParentId()));
            category.setParent(parentCategory);
            category.setDepth(parentCategory.getDepth() + 1);
            parentCategory.addChild(category);
        } else {
            category.setDepth(1L);
            category.setParent(null);
        }
    }

    public List<CategoryTreeResponse> buildCategoryTree(List<CategoryFlatProjection> flatCategories) {
        Map<Long, CategoryTreeResponse> map = new HashMap<>();
        List<CategoryTreeResponse> roots = new ArrayList<>();

        for (CategoryFlatProjection c : flatCategories) {
            CategoryTreeResponse node = new CategoryTreeResponse(
                    c.getCategoryId(),
                    c.getCategoryName(),
                    c.getDepth(),
                    c.getParentId()
            );
            map.put(c.getCategoryId(), node);
        }

        for (CategoryTreeResponse node : map.values()) {
            Long parentId = node.getParentId();
            // parentId가 null 이거나 자기인 경우
            if (parentId == null || parentId.equals(node.getCategoryId())) {
                roots.add(node);
            } else { // 자식일경우
                CategoryTreeResponse parent = map.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    throw new CategoryNotFoundException(node.getCategoryId(), parentId);
                }
            }
        }

        return roots;
    }

    private CategoryTreeResponse findNodeById(List<CategoryTreeResponse> nodes, Long id) {
        for (CategoryTreeResponse node : nodes) {
            if (node.getCategoryId().equals(id)) {
                return node;
            }
            CategoryTreeResponse found = findNodeById(node.getChildren(), id);
            if (found != null) { // 자식에서 찾았다면 반환
                return found;
            }
        }
        return null;
    }

    private boolean isCyclicParent(Category category, Long newParentId) {
        if (newParentId == null) return false;

        if (category.getId().equals(newParentId)) return true; // 자기 자신

        Category parent = categoryRepository.findById(newParentId).orElse(null);
        Set<Long> visited = new HashSet<>();
        while (parent != null) {
            if (!visited.add(parent.getId())) {
                return true; // 순환 감지
            }
            if (parent.getId().equals(category.getId())) {
                return true; // 자식이 부모로 올라가는 경우
            }
            parent = parent.getParent();
        }
        return false;
    }
}
