package com.christos_bramis.bram_vortex_Oauth2.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {

// make a JWT for the user and the salted hash password
    private final long validityInMs = 3600000;            // 1 hour
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public JwtTokenProvider(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public String createToken(String username, String githubToken) {        // possible createSecret to be renamed
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("github_token", githubToken);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
//                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken oauthToken) {
        return authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );    }
}
