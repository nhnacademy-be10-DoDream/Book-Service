package shop.dodream.book.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.annotation.ValidatedReviewFiles;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ReviewFilesValidator implements ConstraintValidator<ValidatedReviewFiles, List<MultipartFile>> {
    private static final long MAX_FILE_SIZE = 5242880;
    private static final Set<String> ALLOWED_IMAGE_MIME_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png"
    );
    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png"
    );

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (Objects.isNull(files)) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();

            if (!StringUtils.hasText(filename)) {
                context.buildConstraintViolationWithTemplate("파일 이름이 비어있습니다.")
                        .addConstraintViolation();
                valid = false;
                continue;
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                context.buildConstraintViolationWithTemplate(String.format("%s : 파일 크기는 %dMB를 초과할 수 없습니다.", filename, + (MAX_FILE_SIZE / 1024 / 1024)))
                        .addConstraintViolation();
                valid = false;
            }

            String extension = getExtension(filename);
            if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
                context.buildConstraintViolationWithTemplate(String.format("%s : 허용되지 않는 확장자입니다. (허용: %s)", filename, ALLOWED_IMAGE_EXTENSIONS))
                        .addConstraintViolation();
                valid = false;
            }

            String contentType = file.getContentType();
            if (Objects.isNull(contentType) || !ALLOWED_IMAGE_MIME_TYPES.contains(contentType.toLowerCase())) {
                context.buildConstraintViolationWithTemplate(String.format("%s : 허용되지 않는 MIME 형식입니다. (허용: %s)", contentType, ALLOWED_IMAGE_MIME_TYPES))
                        .addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return filename.substring(dotIndex + 1);
    }
}
