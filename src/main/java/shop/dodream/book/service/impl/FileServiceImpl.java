package shop.dodream.book.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl {
    private final S3Client s3Client;
    @Value("${minio.review-bucket}")
    private String bucketName;


    public List<String> uploadFiles(List<MultipartFile> files) {
        if (Objects.isNull(files) || files.isEmpty()) {
            return List.of();
        }
        List<String> uploadedKeys = new ArrayList<>(files.size());

        for (MultipartFile file : files) {
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String key = UUID.randomUUID() + extension;
            try {
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(key)
                                .contentType(file.getContentType())
                                .contentLength(file.getSize())
                                .build(),
                        RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                );

                uploadedKeys.add(key);
            } catch (Exception e) {
                log.error("파일 저장 실패");
                // 적절한 로직 필요
            }
        }

        return uploadedKeys;
    }

    public void deleteFiles(List<String> keys) {
        for (String key : keys) {
            try {
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build());
            } catch (Exception e) {
                log.error("삭제 실패: {}", key);
            }
        }
    }

}
