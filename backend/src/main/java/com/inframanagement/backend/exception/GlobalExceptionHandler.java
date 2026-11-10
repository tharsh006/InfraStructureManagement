package com.inframanagement.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<Map<String, String>> handleBadRequest(
        IllegalArgumentException exception) {

    Map<String, String> error = Map.of(
            "error", "Bad Request",
            "message", exception.getMessage()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProjectNotFound(
            ProjectNotFoundException exception) {

        Map<String, String> error = Map.of(
                "error", "Not Found",
                "message", exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EnvironmentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEnvironmentNotFound(
            EnvironmentNotFoundException exception) {

        Map<String, String> error = Map.of(
                "error", "Not Found",
                "message", exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}