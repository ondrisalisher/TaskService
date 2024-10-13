package com.example.TaskService.controllerAdvice;

import com.example.TaskService.exceptions.AppError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AppError> handleRuntimeException(RuntimeException ex) {
        AppError error = new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        log.warn(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
