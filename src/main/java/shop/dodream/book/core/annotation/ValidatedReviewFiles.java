package shop.dodream.book.core.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import shop.dodream.book.core.validator.ReviewFilesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReviewFilesValidator.class)
public @interface ValidatedReviewFiles {
    String message() default "유효하지 않은 파일입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
