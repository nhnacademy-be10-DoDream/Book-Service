package shop.dodream.book.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String uploadBookImageFromUrl(String imageUrl);

    List<String> uploadBookImageFromFiles(List<MultipartFile> files);

    void deleteBookImage(List<String> imgUrls);

    List<String> uploadReviewImageFromFiles(List<MultipartFile> files);

    void deleteReviewImage(List<String> imgUrls);
}