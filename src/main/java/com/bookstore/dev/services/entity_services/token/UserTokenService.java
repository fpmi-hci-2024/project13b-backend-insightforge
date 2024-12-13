package com.bookstore.dev.services.entity_services.token;

import com.bookstore.dev.configs.exception.ApiException;
import com.bookstore.dev.domain.dto.LoginResponse;
import com.bookstore.dev.domain.entities.users.User;
import com.bookstore.dev.domain.entities.users.UserToken;
import com.bookstore.dev.domain.repositories.UserTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UserTokenService {
    UserTokenRepository userTokenRepository;
    @Value("${jwt.accessExpirationMs}")
    private long accessExpirationMs;

    public UserTokenService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    public LoginResponse createNewToken(String access, String refresh, User user) {
        var savedToken = userTokenRepository.save(UserToken.builder()
                .access(access)
                .refresh(refresh)
                .user(user)
                .expiresDateTime(LocalDateTime.now().plus(accessExpirationMs, ChronoUnit.MILLIS))
                .createdDateTime(LocalDateTime.now())
                .build());
        return LoginResponse.builder()
                .detail("OK")
                .accessToken(savedToken.getAccess())
                .refreshToken(savedToken.getRefresh())
                .build();
    }

    public void deleteTokenIfExist(String token) {
        userTokenRepository
                .findByAccess(token)
                .ifPresent(findToken -> {
                    userTokenRepository.delete(findToken);
                });
    }

    public LoginResponse refreshToken(String refreshToken, String newAccessToken, String newRefreshToken) {
        var findToken = userTokenRepository
                .findByRefresh(refreshToken)
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "Invalid refresh token."));

        var createdToken = userTokenRepository.save(UserToken.builder()
                .access(newAccessToken)
                .refresh(newRefreshToken)
                .user(findToken.getUser())
                .expiresDateTime(LocalDateTime.now().plus(accessExpirationMs, ChronoUnit.MILLIS))
                .createdDateTime(LocalDateTime.now())
                .build());

        userTokenRepository.delete(findToken);

        return LoginResponse.builder()
                .detail("OK")
                .accessToken(createdToken.getAccess())
                .refreshToken(createdToken.getRefresh())
                .build();
    }
}
