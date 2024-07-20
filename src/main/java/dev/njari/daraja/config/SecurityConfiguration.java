package dev.njari.daraja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author njari_mathenge
 * on 19/07/2024.
 * github.com/iannjari
 */

@Configuration
public class SecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
            "/management/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(AUTH_WHITELIST).permitAll()).build();
    }
}
