package shop.dodream.book.exception;

import java.util.Set;

public class CategoryAlreadyRegisteredException extends ResourceConflictException {
    public CategoryAlreadyRegisteredException(Set<Long> duplicateIds) {
        super("이미 등록된 카테고리입니다: " + duplicateIds);
    }
    public CategoryAlreadyRegisteredException(String categoryName) { super("이미 등록된 카테고리입니다: " + categoryName); }
}
