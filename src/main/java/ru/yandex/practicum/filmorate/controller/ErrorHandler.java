package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NoSuchElementException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;

@RestControllerAdvice("ru.yandex.practicum.filmorate")
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationException(final ValidationException e) {
        return new ResponseEntity<>(
                Map.of("ValidationException", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(final NoSuchElementException e) {
        return new ResponseEntity<>(
                Map.of("NoSuchElementException", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleThrowable(final Throwable e) {
        String message = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
        return new ResponseEntity<>(
                Map.of("Throwable", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
