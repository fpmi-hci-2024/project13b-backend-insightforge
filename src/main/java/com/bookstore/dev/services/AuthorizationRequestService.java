package com.bookstore.dev.services;

import com.bookstore.dev.configs.exception.ApiException;
import com.bookstore.dev.domain.repositories.UserRepository;
import com.bookstore.dev.domain.repositories.UserTokenRepository;
import com.bookstore.dev.services.utils.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizationRequestService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserAuthenticateService userAuthenticateService;
    private final JwtService jwtService;
    public static final String HEADER_NAME_WITH_TOKEN = "Authorization";

    private void setAnonymousUserAuthentication(HttpServletRequest request) {
        userAuthenticateService.setAuthenticatedUserToSecurityContext("anonymousUser", request);
    }

    public void authUserWithToken(HttpServletRequest request) {
        extract(request, HEADER_NAME_WITH_TOKEN)
                .ifPresentOrElse(token -> {
                    var tokenFromRepository = userTokenRepository
                            .findByAccess(token)
                            .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Недопустимый токен."));

                    jwtService.validateToken(token);

                    userAuthenticateService.setAuthenticatedUserToSecurityContext(String.valueOf(tokenFromRepository.getUser().getId()), request);
                }, () -> {
                    throw new ApiException(HttpStatus.UNAUTHORIZED, "Токен отсутствует в хедере");
                });
    }

    public Optional<String> extract(HttpServletRequest request, String headerName) {
        String value = request.getHeader(headerName);
        if (isEmpty(value)) {
            return Optional.empty();
        }

        return Optional.of(value);
    }
}
