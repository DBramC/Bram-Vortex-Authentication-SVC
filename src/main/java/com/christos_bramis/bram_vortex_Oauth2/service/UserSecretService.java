package com.christos_bramis.bram_vortex_Oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserSecretService {

    @Autowired
    private VaultTemplate vaultTemplate;

    /**
     * Saves Token to Vault (KV Version 1) ( for KV Version 2 add /data )
     * Path: secret/users/{username}
     */
    public void saveUserToken(String username, String accessToken) {

        Map<String, String> secrets = new HashMap<>();
        secrets.put("github_token", accessToken);

        vaultTemplate.write("secret/users/" + username, secrets);

        System.out.println("✅ Token saved securely to Vault for user: " + username);
    }


}