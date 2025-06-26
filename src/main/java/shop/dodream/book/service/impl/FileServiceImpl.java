package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.service.FileService;
import shop.dodream.book.util.MinioUploader;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value("${minio.bucket}")
    private String bucketName;
    @Value("${minio.review-prefix}")
    private String bookPrefix;
    @Value("${minio.book-prefix}")
    private String reviewPrefix;

    private final MinioUploader minioUploader;

    public String uploadBookImageFromUrl(String imageUrl) throws IOException {
        String keyPrefix = getBookPrefix();
        return minioUploader.uploadFromUrl(bucketName, keyPrefix, imageUrl);
    }

    private List<String> uploadFiles(List<MultipartFile> files, Supplier<String> prefixGenerator) {
        if (isEmptyFileList(files)) {
            return List.of();
        }
        String keyPrefix = prefixGenerator.get();
        return minioUploader.uploadFiles(bucketName, keyPrefix, files);
    }

    private void deleteFiles(List<String> imgUrls, Supplier<String> prefixGenerator) {
        if (isEmptyStringList(imgUrls)) {
            return;
        }
        String keyPrefix = prefixGenerator.get();
        minioUploader.deleteFiles(bucketName, keyPrefix, imgUrls);
    }

    public List<String> uploadBookImageFromFiles(List<MultipartFile> files) {
        return uploadFiles(files, this::getBookPrefix);
    }

    public void deleteBookImage(List<String> imgUrls) {
        deleteFiles(imgUrls, this::getBookPrefix);
    }

    public List<String> uploadReviewImageFromFiles(List<MultipartFile> files) {
        return uploadFiles(files, this::getReviewPrefix);
    }

    public void deleteReviewImage(List<String> imgUrls) {
        deleteFiles(imgUrls, this::getReviewPrefix);
    }

    private boolean isEmptyFileList(List<MultipartFile> files) {
        return Objects.isNull(files) || files.isEmpty();
    }

    private boolean isEmptyStringList(List<String> list) {
        return Objects.isNull(list) || list.isEmpty();
    }

    private String getBookPrefix() {
        return String.format("/%s/", bookPrefix);
    }

    private String getReviewPrefix() {
        return String.format("/%s/", reviewPrefix);
    }
}
