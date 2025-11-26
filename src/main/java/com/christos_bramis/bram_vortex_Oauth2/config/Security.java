package com.christos_bramis.bram_vortex_Oauth2.config;

import com.christos_bramis.bram_vortex_Oauth2.jwt.GitHubJwtSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           GitHubJwtSuccessHandler successHandler) throws Exception {
        http
                // 1. Disable CSRF (Correct for Stateless APIs & JWT)
                // This is not a shortcut; it is the standard architectural choice for REST APIs
                // where state is handled by tokens, not session cookies.
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configure Stateless Session Management
                // We tell Spring: "Do not hold session memory for the user."
                // Every request must possess identity (Token) or login from scratch.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. Authorization Settings
                .authorizeHttpRequests(auth -> auth
                        // Allow everyone to see the login page and oauth endpoints
                        .requestMatchers("/", "/login**", "/error", "/oauth2/**").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // 4. OAuth2 Login Configuration
                .oauth2Login(oauth2 -> oauth2
                        // Hook up the Handler we created (Dependency Injection)
                        .successHandler(successHandler)
                );

        return http.build();
    }
}