package com.christos_bramis.bram_vortex_Oauth2.controller;

import com.christos_bramis.bram_vortex_Oauth2.service.UserSecretService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/credentials")
public class CredentialsController {

    private final UserSecretService userSecretService;

    public CredentialsController(UserSecretService userSecretService) {
        this.userSecretService = userSecretService;
    }

    @GetMapping("/check/{provider}")
    public ResponseEntity<Map<String, Boolean>> check(@PathVariable String provider, @RequestAttribute("username") String username) {
        return ResponseEntity.ok(Map.of("exists", userSecretService.hasCloudCredentials(username, provider)));
    }

    @PostMapping("/aws")
    public ResponseEntity<?> saveAws(@RequestBody Map<String, String> keys, @RequestAttribute("username") String username) {
        // Το region ΔΕΝ αποθηκεύεται εδώ, καθώς είναι Global το IAM key
        userSecretService.saveCloudCredentials(username, "aws", keys);
        return ResponseEntity.ok("AWS Linked");
    }
}