package com.bookstore.dev.services.user_services;

import com.bookstore.dev.configs.exception.ApiException;
import com.bookstore.dev.domain.dto.LoginRequest;
import com.bookstore.dev.domain.dto.LoginResponse;
import com.bookstore.dev.domain.dto.UserRegistrationRequest;
import com.bookstore.dev.domain.entities.users.EUserRole;
import com.bookstore.dev.domain.entities.users.User;
import com.bookstore.dev.domain.repositories.RoleRepository;
import com.bookstore.dev.domain.repositories.UserRepository;
import com.bookstore.dev.services.entity_services.token.UserTokenService;
import com.bookstore.dev.services.utils.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.bookstore.dev.services.utils.security.PasswordUtils.isPasswordRight;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserTokenService userTokenService;

    public User registerNewUser(UserRegistrationRequest user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ApiException(HttpStatus.CONFLICT, "User with username " + user.getUsername() + " exists");
        }

        var userRole = roleRepository.findByTitle(EUserRole.ROLE_USER)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Role for User not found"));
        return userRepository.save(User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .enabled(true)
                .roles(Set.of(userRole))
                .build());
    }

    public LoginResponse loginUser(LoginRequest userLoginRequest) {
        var user = userRepository.findByUsername(userLoginRequest.getUsername())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (!isPasswordRight(userLoginRequest.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Password incorrect");
        }

        String accessToken = jwtService.createJwtAccessToken(user.getUsername());
        String refreshToken = jwtService.createJwtRefreshToken(user.getUsername());

        return userTokenService.createNewToken(accessToken, refreshToken, user);
    }
}
