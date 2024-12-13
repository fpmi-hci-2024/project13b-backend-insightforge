package com.bookstore.dev.services.utils.security;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TokenGeneratorService {
    public static final String CHARACTERS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final String CHARACTERS_ONLY_LETTERS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final char[] SYMBOLS = CHARACTERS.toCharArray();
    private static final char[] LETTERS = CHARACTERS_ONLY_LETTERS.toCharArray();
    private final SecureRandom random = new SecureRandom();

    public String generateToken(int length) {
        char[] tokenChars = new char[length];
        for (int index = 0; index < tokenChars.length; index++) {
            tokenChars[index] = SYMBOLS[random.nextInt(SYMBOLS.length)];
        }
        return new String(tokenChars);
    }

    public String generateLettersToken(int length) {
        char[] tokenChars = new char[length];
        for (int index = 0; index < tokenChars.length; index++) {
            tokenChars[index] = LETTERS[random.nextInt(LETTERS.length)];
        }
        return new String(tokenChars);
    }
}