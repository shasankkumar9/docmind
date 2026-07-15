package com.ssharma.docmind.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DocMindException.class)
    public ResponseEntity<ApiError> handleDocMindException(
            DocMindException ex) {

        LOGGER.warn("{} - {}", ex.getErrorCode().code(), ex.getMessage());

        HttpStatus status = switch (ex.getErrorCode()) {

            case DOCUMENT_NOT_FOUND -> HttpStatus.NOT_FOUND;

            case UNSUPPORTED_FILE_TYPE -> HttpStatus.UNSUPPORTED_MEDIA_TYPE;

            case VALIDATION_ERROR,
                 DOCUMENT_PARSING_FAILED -> HttpStatus.BAD_REQUEST;

            case EMBEDDING_FAILED,
                 RETRIEVAL_FAILED,
                 CHAT_FAILED,
                 INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;

        };

        return ResponseEntity.status(status)
                .body(new ApiError(
                        ex.getErrorCode(),
                        ex.getMessage(),
                        now()
                ));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        LOGGER.warn("Validation failed: {}", message);

        return ResponseEntity.badRequest()
                .body(new ApiError(
                        ErrorCode.VALIDATION_ERROR,
                        message,
                        now()
                ));

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex) {

        LOGGER.warn("Constraint violation: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(new ApiError(
                        ErrorCode.VALIDATION_ERROR,
                        ex.getMessage(),
                        now()
                ));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception ex) {

        LOGGER.error("Unexpected exception", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred. Please try again later.",
                        now()
                ));

    }

    private LocalDateTime now() {
        return LocalDateTime.now(Clock.systemDefaultZone());
    }

}