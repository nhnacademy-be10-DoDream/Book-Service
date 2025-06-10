package shop.dodream.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String title;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(String title, int status, LocalDateTime timestamp) {
        this.title = title;
        this.status = status;
        this.timestamp = timestamp;
    }
}

