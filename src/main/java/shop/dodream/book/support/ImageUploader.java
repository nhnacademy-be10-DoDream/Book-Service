package shop.dodream.book.support;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageUploader {


    @Value("${upload.book-image-dir}")
    private String uploadDir;

    public String uploadFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            String fileName = UUID.randomUUID() + "_book.jpg";
            Path targetPath = Paths.get(uploadDir, fileName);
            Files.createDirectories(targetPath.getParent()); // 폴더 없으면 생성
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "/images/books/" + fileName;
        }
    }
}
