package com.bookstore.dev.services.utils.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

public class TokenProcessor {
    public static boolean isTokenExpire(LocalDateTime expires) {
        return expires.isBefore(LocalDateTime.now(ZoneOffset.UTC));
    }

    public static Optional<String> trimTokenPrefix(String token, String prefix) {
        if (Objects.isNull(token) || token.isEmpty() || !token.startsWith(prefix)) {
            return Optional.empty();
        }

        return Optional.of(token.replace(prefix, ""));
    }
}
