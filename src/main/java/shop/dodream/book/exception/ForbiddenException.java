package shop.dodream.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;
import org.springframework.web.ErrorResponse;

public class ForbiddenException extends RuntimeException implements ErrorResponse {

    private final transient ProblemDetail body;

    public ForbiddenException() {
        this("Forbidden");
    }

    public ForbiddenException(String message) {
        this(message, null);
    }

    public ForbiddenException(String message, @Nullable Throwable cause) {
        super(message, cause);
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), message);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public ProblemDetail getBody() {
        return this.body;
    }
}