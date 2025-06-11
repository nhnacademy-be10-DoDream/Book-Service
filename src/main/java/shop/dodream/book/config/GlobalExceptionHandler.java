package shop.dodream.book.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.dodream.book.dto.ErrorResponse;
import shop.dodream.book.exception.BookIdNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserIdEmptyError(BookIdNotFoundException e) {
        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
