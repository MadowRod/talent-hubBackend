package br.com.talen_hub.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationErrorResponse(
        int status,
        String error,
        List<String> errors,
        LocalDateTime timestamp
) {
}