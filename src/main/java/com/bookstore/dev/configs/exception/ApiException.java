package com.bookstore.dev.configs.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private String detail;
}