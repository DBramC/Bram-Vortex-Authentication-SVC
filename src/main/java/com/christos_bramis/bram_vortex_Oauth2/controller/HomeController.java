package com.christos_bramis.bram_vortex_Oauth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal,
                       @RequestParam(name = "token", required = false) String token) {

        // ΠΕΡΙΠΤΩΣΗ A: Μόλις γύρισες από το Login (έχεις token στο URL)
        if (token != null && !token.isEmpty()) {
            return """
                <div style='font-family: monospace; padding: 20px;'>
                    <h2 style='color: green;'>✅ Login Successful!</h2>
                    <p>Here is your signed JWT (save it):</p>
                    <textarea rows='10' cols='100' readonly>%s</textarea>
                    <br><br>
                    <a href='/'>Go to Home</a>
                </div>
                """.formatted(token);
        }

        // ΠΕΡΙΠΤΩΣΗ B: Είσαι απλά Logged in (αλλά χωρίς token στο URL)
        if (principal != null) {
            return "<h1>Welcome back, " + principal.getAttribute("login") + "!</h1>";
        }

        // ΠΕΡΙΠΤΩΣΗ Γ: Δεν είσαι Logged in (Guest)
        else {
            return """
                <h1>Bram Vortex Auth Service</h1>
                <p>Status: Running</p>
                <a href='/oauth2/authorization/github' style='font-size: 20px; background: black; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>
                    Login with GitHub
                </a>
                """;
        }
    }
}