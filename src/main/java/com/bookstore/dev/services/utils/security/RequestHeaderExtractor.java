package com.bookstore.dev.services.utils.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class RequestHeaderExtractor {

    public Optional<String> extract(HttpServletRequest request, String headerName) {
        String value = request.getHeader(headerName);
        if (isEmpty(value)) {
            return Optional.empty();
        }

        return Optional.of(value);
    }
}
