package shop.dodream.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;

public class ResourceNotFoundException extends RuntimeException implements ErrorResponse {

    private final transient ProblemDetail body;

    public ResourceNotFoundException() {
        this("Resource not found.");
    }

    public ResourceNotFoundException(String message) {
        this(message, null);
    }

    public ResourceNotFoundException(String message, @Nullable Throwable cause) {
        super(message, cause);
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ProblemDetail getBody() {
        return this.body;
    }

}
