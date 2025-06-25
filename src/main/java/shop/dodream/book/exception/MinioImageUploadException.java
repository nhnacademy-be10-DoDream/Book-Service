package shop.dodream.book.exception;

public class MinioImageUploadException extends RuntimeException {
    public MinioImageUploadException() {
        super("MinIO 업로드 중 오류 발생");
    }
}