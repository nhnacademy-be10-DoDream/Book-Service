package shop.dodream.book.core.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import shop.dodream.book.exception.ForbiddenException;
import shop.dodream.book.exception.ResourceConflictException;
import shop.dodream.book.exception.ResourceNotFoundException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 400 Bad Request
     * MethodArgumentNotValidException 공통 처리
     */
    @Nullable
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        URI type = URI.create("/errors/method-argument-not-valid");
        String detail = "Invalid request content.";

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ProblemDetail body = createProblemDetail(ex, status, detail, null, null, request);
        body.setType(type);
        body.setProperty("invalidFields", errors);

        return handleExceptionInternal(ex, body, headers, status, request);
    }



    /**
     * 403 Forbidden
     * ForbiddenException 공통 처리
     */
    @Nullable
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbidden(ForbiddenException ex, WebRequest request) {
        URI type = URI.create("/errors/forbidden");
        HttpStatusCode status = ex.getStatusCode();
        String detail = ex.getLocalizedMessage();

        ProblemDetail body = createProblemDetail(ex, status, detail, null, null, request);
        body.setType(type);

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    /**
     * 404 Not Found
     * ResourceNotFoundException 공통 처리
     */
    @Nullable
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        URI type = URI.create("/errors/not-found");
        HttpStatusCode status = ex.getStatusCode();
        String detail = ex.getLocalizedMessage();

        ProblemDetail body = createProblemDetail(ex, status, detail, null, null, request);
        body.setType(type);

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    /**
     * 409 Conflict
     * IllegalStateException 공통 처리
     */
    @Nullable
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Object> handleIllegalState(ResourceConflictException ex, WebRequest request) {
        URI type = URI.create("/errors/conflict");
        HttpStatusCode status = HttpStatus.CONFLICT;
        String detail = ex.getLocalizedMessage();

        ProblemDetail body = createProblemDetail(ex, status, detail, null, null, request);
        body.setType(type);

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    /**
     * 아무 핸들러에도 걸리지 않은 예외를 미분류 500 에러로 일괄 처리
     */
    @Nullable
    @ExceptionHandler
    public ResponseEntity<Object> handleUnclassified(Exception ex, WebRequest request) {
        URI type = URI.create("/errors/unclassified");
        HttpStatusCode status = HttpStatus.INTERNAL_SERVER_ERROR;
        // 적절히 처리되지 않은 예외의 상세 내용을 노출하지 않기 위해 비어있는 문자열을 전달
        String detail = "";

        ProblemDetail body = createProblemDetail(ex, status, detail, null, null, request);
        body.setType(type);

        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode statusCode,
                                                             WebRequest request) {
        if (statusCode.is5xxServerError()) {
            log.error(ex.getLocalizedMessage(), ex);
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {
        URI type = URI.create("/errors/method-argument-not-valid");
        String detail = "Invalid request content.";

        List<String> errors = new ArrayList<>();
        for (ParameterValidationResult result : ex.getAllValidationResults()) {
            for (MessageSourceResolvable resolvable : result.getResolvableErrors()) {
                errors.add(resolvable.getDefaultMessage());
            }
        }

        ProblemDetail body = createProblemDetail(ex, status, detail, null, null, request);
        body.setType(type);
        body.setProperty("invalidFiles", errors);

        return handleExceptionInternal(ex, body, headers, status, request);
    }

}