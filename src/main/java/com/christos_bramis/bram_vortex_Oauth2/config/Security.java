package com.christos_bramis.bram_vortex_Oauth2.config;

import com.christos_bramis.bram_vortex_Oauth2.jwt.GitHubJwtSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class Security {

    @Value("${app.frontend.url}")
    private String frontendUrlBase;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           GitHubJwtSuccessHandler successHandler) throws Exception {
        http
                // 1. ΕΝΕΡΓΟΠΟΙΗΣΗ CORS (ΣΗΜΑΝΤΙΚΟ!)
                // Λέμε στο Spring να χρησιμοποιήσει το Bean που φτιάξαμε παρακάτω
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Disable CSRF (Correct for Stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Stateless Session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login**", "/error", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 5. OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler)
                );

        return http.build();
    }

    // 6. ΟΡΙΣΜΟΣ ΤΩΝ ΚΑΝΟΝΩΝ CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Επιτρέπουμε το Frontend (Vite)
        configuration.setAllowedOrigins(List.of(frontendUrlBase));

        // Επιτρέπουμε όλες τις μεθόδους (GET, POST, κλπ)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Επιτρέπουμε headers (π.χ. Authorization για το JWT)
        configuration.setAllowedHeaders(List.of("*"));

        // Επιτρέπουμε credentials (αν χρειαστεί)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}