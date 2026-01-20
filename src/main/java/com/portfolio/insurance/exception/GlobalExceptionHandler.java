package com.portfolio.insurance.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                request.getRequestURI(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Erro de validação",
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, HttpServletRequest request) {
        List<String> details = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Erro de validação",
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Erro interno",
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
