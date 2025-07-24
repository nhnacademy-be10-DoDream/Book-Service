package shop.dodream.book.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import shop.dodream.book.exception.MinioImageUploadException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioUploaderTest {

    @Mock
    S3Client s3Client;

    @InjectMocks
    MinioUploader minioUploader;


    @Test
    @DisplayName("uploadFiles - 파일 여러 개 업로드 성공")
    void uploadFiles_success() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.png", "image/png", "abc".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpg", "def".getBytes());

        List<String> keys = minioUploader.uploadFiles("test-bucket", "prefix/", List.of(file1, file2));
        verify(s3Client, times(2)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertThat(keys).hasSize(2);
    }

    @Test
    @DisplayName("deleteFiles - 정상 삭제")
    void deleteFiles_success() {
        List<String> keys = List.of("img1.png", "img2.jpg");

        minioUploader.deleteFiles("test-bucket", "prefix/", keys);

        verify(s3Client, times(2)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    @DisplayName("uploadFiles - 업로드 중 실패하면 예외 발생 및 삭제 시도")
    void uploadFiles_shouldDeleteOnFailure() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.png", "image/png", "abc".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpg", "def".getBytes());

        doThrow(RuntimeException.class).when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        assertThatThrownBy(() -> minioUploader.uploadFiles("test-bucket", "prefix/", List.of(file1, file2)))
                .isInstanceOf(MinioImageUploadException.class);

        verify(s3Client, atLeastOnce()).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }
}
