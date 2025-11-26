package com.christos_bramis.bram_vortex_Oauth2.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenProvider {

    private final VaultTemplate vaultTemplate;
    private final ObjectMapper objectMapper;

    private final long validityInMs = 3600000; // 1 hour
    private static final String TRANSIT_KEY_NAME = "jwt-signing-key";

    @Autowired
    public JwtTokenProvider(VaultTemplate vaultTemplate, ObjectMapper objectMapper) {
        this.vaultTemplate = vaultTemplate;
        this.objectMapper = objectMapper;
    }

    public String createToken(String username) {
        try {
            // 1. Header
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "RS256");
            header.put("typ", "JWT");

            // 2. Payload (Claims)
            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", username);
            payload.put("roles", new String[]{"USER", "MEMBER"});

            Date now = new Date();
            Date expiry = new Date(now.getTime() + validityInMs);
            payload.put("iat", now.getTime() / 1000);
            payload.put("exp", expiry.getTime() / 1000);

            // 3. Encoding Header.Payload
            String encodedHeader = toBase64Url(objectMapper.writeValueAsString(header).getBytes());
            String encodedPayload = toBase64Url(objectMapper.writeValueAsString(payload).getBytes());
            String dataToSign = encodedHeader + "." + encodedPayload;

            // 4. Αποστολή στο Vault (Χειροκίνητα με Map)
            // Το Transit API θέλει το input σε απλό Base64
            String inputBase64 = Base64.getEncoder().encodeToString(dataToSign.getBytes(StandardCharsets.UTF_8));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("input", inputBase64);


            VaultResponse response = vaultTemplate.write("transit/sign/" + TRANSIT_KEY_NAME, requestBody);

            // 5. Λήψη Υπογραφής
            assert response != null;
            Map<String, Object> data = response.getData();
            assert data != null;
            String vaultSignature = (String) data.get("signature"); // "vault:v1:XYZ..."

            // 6. Καθαρισμός Υπογραφής
            String rawSignature = vaultSignature.substring(vaultSignature.lastIndexOf(":") + 1);
            // Μετατροπή από Vault Base64 σε JWT Base64Url
            byte[] sigBytes = Base64.getDecoder().decode(rawSignature);
            String jwtSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(sigBytes);

            // 7. Τελικό Token
            return dataToSign + "." + jwtSignature;

        } catch (Exception e) {
            throw new RuntimeException("Error signing JWT with Vault", e);
        }
    }

    private String toBase64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}