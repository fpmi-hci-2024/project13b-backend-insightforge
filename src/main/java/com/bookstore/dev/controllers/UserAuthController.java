package com.bookstore.dev.controllers;

import com.bookstore.dev.domain.dto.LoginRequest;
import com.bookstore.dev.domain.dto.LoginResponse;
import com.bookstore.dev.domain.dto.UserRegistrationRequest;
import com.bookstore.dev.services.user_services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserAuthController {
    private final UserService userService;
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/registration")
    public void registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        userService.registerNewUser(userRegistrationRequest);
    }
    @Operation(summary = "Вход пользователя")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }
}
