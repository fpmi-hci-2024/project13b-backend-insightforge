package com.bookstore.dev.configs.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> handleApplicationException(ApiException apiException) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                .detail(apiException.getDetail())
                .build(), apiException.getStatus());
    }
}
