package shop.dodream.book.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.exception.MinioImageUploadException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUploader {
    private final S3Client s3Client;

    public String uploadFromUrl(String bucketName, String keyPrefix, String imageUrl) {
        try {
            URL url = URI.create(imageUrl).toURL();
            String key = UUID.randomUUID() + ".png";
            try (InputStream inputStream = url.openStream()) {
                byte[] content = inputStream.readAllBytes();

                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(keyPrefix + key)
                                .contentType("image/png")
                                .contentLength((long) content.length)
                                .build(),
                        RequestBody.fromBytes(content)
                );
                return key;
            }
        }catch (Exception e) {
            log.error("MinIO 오류 발생 : {}", e.getMessage());
            throw new MinioImageUploadException();
        }
    }

    public List<String> uploadFiles(String bucketName, String keyPrefix, List<MultipartFile> files) {
        List<String> uploadedKeys = new ArrayList<>(files.size());

        for (MultipartFile file : files) {
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String key = UUID.randomUUID() + extension;
            try {
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(keyPrefix + key)
                                .contentType(file.getContentType())
                                .contentLength(file.getSize())
                                .build(),
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                );

                uploadedKeys.add(key);
            } catch (Exception e) {
                log.error("파일 저장 실패(경로 : {}, 파일명: {})", keyPrefix, key);
            }
        }

        return uploadedKeys;
    }

    public void deleteFiles(String bucketName, String keyPrefix, List<String> keys) {
        for (String key : keys) {
            try {
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(keyPrefix + key)
                        .build());
            } catch (Exception e) {
                log.error("삭제 실패 (경로 : {}, 파일명: {})", keyPrefix, key);
            }
        }
    }
}
