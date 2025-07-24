package shop.dodream.book.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.dodream.book.core.properties.MinIOProperties;
import shop.dodream.book.service.impl.FileServiceImpl;
import shop.dodream.book.util.MinioUploader;

import java.util.List;

import static org.mockito.Mockito.*;

class FileServiceTest {

    @Mock
    private MinioUploader minioUploader;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MinIOProperties props = new MinIOProperties();
        props.setBucket("test-bucket");
        props.setBookPrefix("book");
        props.setReviewPrefix("review");
        ReflectionTestUtils.setField(fileService, "ioProperties", props);
    }

    @Test
    void uploadBookImageFromFiles_shouldCallUploader_whenFilesPresent() {
        List<MultipartFile> files = List.of(mockFile);

        fileService.uploadBookImageFromFiles(files);

        verify(minioUploader, times(1))
                .uploadFiles(eq("test-bucket"), eq("/book/"), eq(files));
    }

    @Test
    void uploadBookImageFromFiles_shouldNotCallUploader_whenFilesEmpty() {
        fileService.uploadBookImageFromFiles(List.of());

        verify(minioUploader, never()).uploadFiles(any(), any(), any());
    }

    @Test
    void deleteReviewImage_shouldCallUploader_whenUrlsPresent() {
        List<String> urls = List.of("review/123.jpg");

        fileService.deleteReviewImage(urls);

        verify(minioUploader, times(1))
                .deleteFiles(eq("test-bucket"), eq("/review/"), eq(urls));
    }

    @Test
    void deleteReviewImage_shouldNotCallUploader_whenUrlsEmpty() {
        fileService.deleteReviewImage(List.of());

        verify(minioUploader, never()).deleteFiles(any(), any(), any());
    }

    @Test
    void uploadBookImageFromUrl_shouldCallUploaderWithCorrectParams() {
        String url = "https://image.com/sample.jpg";
        when(minioUploader.uploadFromUrl(any(), any(), any())).thenReturn("result-key");

        String result = fileService.uploadBookImageFromUrl(url);

        verify(minioUploader).uploadFromUrl("test-bucket", "/book/", url);
        assert result.equals("result-key");
    }
}