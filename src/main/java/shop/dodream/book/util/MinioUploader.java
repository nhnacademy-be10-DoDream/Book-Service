package shop.dodream.book.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioUploader {

    private final S3Client s3Client;

    @Value("${minio.book-bucket}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    public String uploadFromUrl(String imageUrl) throws IOException {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            String fileName = UUID.randomUUID() + ".jpg";
            byte[] bytes = inputStream.readAllBytes();


            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType("image/jpeg")
                    .build();

            log.info("PutObject 호출 시작");

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

            log.info("PutObject 호출 완료");
            log.info("PutObject 응답 전체: {}", response);

            return endpoint+"/" + bucketName + "/" + fileName;

        } catch (IOException e) {
            log.error("MinIO 이미지 업로드 실패", e);
            throw e;
        }
    }
}
