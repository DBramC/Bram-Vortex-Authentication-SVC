package com.christos_bramis.bram_vortex_Oauth2;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secretKey = "mySuperSecretKey"; // βάλτο σε env variable
    private final long validityInMs = 3600000; // 1 ώρα

    // Δημιουργία JWT
    public String createToken(String username, String githubToken) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("github_token", githubToken); // αποθηκεύουμε GitHub token στο claim

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Επαλήθευση και ανάγνωση JWT
    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken oauthToken) {
        return null;             // FIX THIS  WORK ON THIS LIVE LAUGH LOVE
    }
}
