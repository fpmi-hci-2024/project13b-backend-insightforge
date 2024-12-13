package com.bookstore.dev.controllers.filter;

import com.bookstore.dev.configs.exception.ApiException;
import com.bookstore.dev.configs.exception.ExceptionResponse;
import com.bookstore.dev.services.AuthorizationRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserAuthFilter extends OncePerRequestFilter {
    private final AuthorizationRequestService authorizationService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            authorizationService.authUserWithToken(request);
        } catch (ApiException exception) {
            writeApiExceptionToResponse(response, exception);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] includePattern = {
                "/api/v1/books.*"
        };

        String path = request.getServletPath();
        boolean matchInclude = Arrays.stream(includePattern)
                .anyMatch(path::matches);

        return !matchInclude;
    }

    private void writeApiExceptionToResponse(HttpServletResponse response, ApiException exception) {
        try {
            response.setStatus(exception.getStatus().value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(convertApiExceptionToStringResponse(exception));
        } catch (Exception e) {

        }
    }

    public static String convertApiExceptionToStringResponse(ApiException apiException) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(ExceptionResponse.builder()
                    .detail(apiException.getDetail())
                    .build());
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
