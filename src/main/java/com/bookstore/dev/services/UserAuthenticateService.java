package com.bookstore.dev.services;

import com.bookstore.dev.domain.entities.users.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
public class UserAuthenticateService {

    public void setAuthenticatedUserToSecurityContext(String userId, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        getContext().setAuthentication(authentication);
    }

    public String getAuthenticatedUserIdFromAuthentication() {
        Authentication authentication = getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser")) return null;
        return authentication.getName();
    }
}
