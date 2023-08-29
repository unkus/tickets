package com.ticketseller.web.exceptions;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> onResponseStatusException(@NotNull ResponseStatusException e) {
        ProblemDetail detail = ProblemDetail.forStatus(e.getStatusCode());
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setDetail(e.getMessage());
        return ResponseEntity.of(detail).build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> onConstraintViolationException(@NotNull ConstraintViolationException e) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setDetail(e.getMessage());
        return ResponseEntity.of(detail).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> onConstraintViolationException(@NotNull MethodArgumentNotValidException e) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setProperty("timestamp", LocalDateTime.now());
        FieldError error = e.getFieldError();
        detail.setDetail(Objects.requireNonNull(error).getField() + ": " + error.getDefaultMessage());
        return ResponseEntity.badRequest().body(detail);
    }

    @ExceptionHandler
    public ResponseEntity<?> onException(Exception e) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setProperty("timestamp", LocalDateTime.now());
        detail.setDetail("Unexpected server error.");
        return ResponseEntity.internalServerError().body(detail);
    }

}
