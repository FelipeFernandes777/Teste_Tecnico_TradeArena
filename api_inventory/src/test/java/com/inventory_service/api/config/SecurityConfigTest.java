package com.inventory_service.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class SecurityConfigTest {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable() // desabilita CSRF
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // libera tudo
                .build();
    }
}
