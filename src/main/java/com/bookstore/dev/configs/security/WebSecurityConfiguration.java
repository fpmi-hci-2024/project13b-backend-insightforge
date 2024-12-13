package com.bookstore.dev.configs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfiguration {
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().and()
                .cors().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher("/v1/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/configuration/ui")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/configuration/security")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/*")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/webjars/**")).permitAll()
                )
                .exceptionHandling();
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
