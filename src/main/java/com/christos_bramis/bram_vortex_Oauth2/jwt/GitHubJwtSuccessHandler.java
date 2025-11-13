package com.christos_bramis.bram_vortex_Oauth2.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class GitHubJwtSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public GitHubJwtSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = jwtTokenProvider.getAuthorizedClient(oauthToken);  // Can delete jwtTokenProvider
        String githubToken = client.getAccessToken().getTokenValue();        // Way to access Token

                                                                            // To be changed for Secrets, only for testing now
        String jwt = jwtTokenProvider.createToken(oauthToken.getName(), githubToken);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + jwt + "\"}");

        System.out.printf(jwt);
    }

}