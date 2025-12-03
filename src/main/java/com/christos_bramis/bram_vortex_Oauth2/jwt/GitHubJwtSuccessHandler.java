package com.christos_bramis.bram_vortex_Oauth2.jwt;

import com.christos_bramis.bram_vortex_Oauth2.service.UserSecretService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubJwtSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend.url}")
    private String frontendUrlBase;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserSecretService userSecretService;
    private final JwtTokenProvider  jwtTokenProvider;

    public GitHubJwtSuccessHandler(OAuth2AuthorizedClientService authorizedClientService,
                                   UserSecretService userSecretService, JwtTokenProvider jwtTokenProvider) {
        this.authorizedClientService = authorizedClientService;
        this.userSecretService = userSecretService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());

        String appJwt = "";

        if (client != null) {
            String githubAccessToken = client.getAccessToken().getTokenValue();
            String username = oauthToken.getPrincipal().getAttribute("login");

            userSecretService.saveUserToken(username, githubAccessToken);
            appJwt = jwtTokenProvider.createToken(username);                 // to be used for the other microservices
        }

        String targetUrl = frontendUrlBase + "/auth-callback?token=" + appJwt;

        response.sendRedirect(targetUrl);
    }
}