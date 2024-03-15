package app.snob.ecommerce_platform.exception.handler;

import app.snob.ecommerce_platform.exception.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    protected final ResponseEntity<Object> resolveException(
            NotFoundException ex, WebRequest request) {
        ApiError apiError =
                new ApiError("Resource was not found.", request.getDescription(false), ex.getMessage());
        log.error(
                "Resource was not found for '{}', returning error message: '{}'",
                request.getDescription(false),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    protected final ResponseEntity<Object> resolveException(
            EntityNotFoundException ex, WebRequest request) {
        ApiError apiError =
                new ApiError("Entity was not found.", request.getDescription(false), ex.getMessage());
        log.error(
                "Entity was not found for '{}', returning error message: '{}'",
                request.getDescription(false),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    protected final ResponseEntity<Object> resolveException(
            BadRequestException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(
                        "Resource was not posted correct.", request.getDescription(false), ex.getMessage());
        log.error(
                "Resource was not posted correctly for '{}', returning error message: '{}'",
                request.getDescription(false),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientInventoryException.class)
    protected final ResponseEntity<Object> resolveException(
            InsufficientInventoryException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(
                        "Insufficient inventory for product.", request.getDescription(false), ex.getMessage());
        log.error(
                "Resource was not posted correctly for '{}', returning error message: '{}'",
                request.getDescription(false),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidEmailException.class)
    protected final ResponseEntity<Object> resolveException(
            InvalidEmailException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(
                        "Email is invalid.", request.getDescription(false), ex.getMessage());
        log.error(
                "Email was not passed correctly for '{}', returning error message: '{}'",
                request.getDescription(false),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationException.class)
    protected final ResponseEntity<Object> resolveException(
            RegistrationException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(
                        "User with the same email exists.", request.getDescription(false), ex.getMessage());
        log.error(
                "Email already exists '{}', returning error message: '{}'",
                request.getDescription(false),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotConfirmedException.class)
    protected final ResponseEntity<Object> resolveException(
            EmailNotConfirmedException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(
                        "User did not confirm his email.", request.getDescription(false), ex.getMessage());
        log.error(
                "User did not confirm his email '{}', returning error message: '{}'",
                request.getDescription(false),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    protected final ResponseEntity<Object> resolveException(
            IllegalArgumentException ex, WebRequest request) {
        ApiError apiError =
                new ApiError(
                        "Method has been passed an illegal or inappropriate argument.", request.getDescription(false), ex.getMessage());
        log.error(
                "Resource was not posted correctly for '{}', returning error message: '{}'",
                request.getDescription(false),
                ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
