package com.bookstore.dev.services.utils.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordUtils {
    private static PasswordEncoder passwordEncoder;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int PASSWORD_LENGTH = 7;

    public PasswordUtils(PasswordEncoder passwordEncoder) {
        PasswordUtils.passwordEncoder = passwordEncoder;
    }

    public static boolean isPasswordRight(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String generate7DigitPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int digit = RANDOM.nextInt(10);
            password.append(digit);
        }
        return password.toString();
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
