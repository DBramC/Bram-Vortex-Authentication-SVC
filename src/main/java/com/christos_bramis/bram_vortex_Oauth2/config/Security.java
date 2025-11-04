package com.christos_bramis.bram_vortex_Oauth2.config;


import com.christos_bramis.bram_vortex_Oauth2.jwt.GitHubJwtSuccessHandler;
import com.christos_bramis.bram_vortex_Oauth2.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(new GitHubJwtSuccessHandler(jwtTokenProvider))
                );

        return http.build();
    }

}
