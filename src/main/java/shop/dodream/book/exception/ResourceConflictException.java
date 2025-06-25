package shop.dodream.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;

public class ResourceConflictException extends RuntimeException implements ErrorResponse {

    private final transient ProblemDetail body;

    public ResourceConflictException() {
        this("Resource is conflict.");
    }

    public ResourceConflictException(String message) {
        this(message, null);
    }

    public ResourceConflictException(String message, @Nullable Throwable cause) {
        super(message, cause);
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public ProblemDetail getBody() {
        return this.body;
    }
}
