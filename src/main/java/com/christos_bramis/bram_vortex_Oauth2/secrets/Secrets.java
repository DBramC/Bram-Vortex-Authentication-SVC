package com.christos_bramis.bram_vortex_Oauth2.secrets;

public class Secrets {

    private final String username;
    private final String password;      // to exclude Symbols that lead to Sql injections

    private final String githubToken;
    private final String cloudProviderToken;   // One for now

    public Secrets(String username, String password, String githubToken, String cloudProviderToken) {
        this.username = username;
        this.password = password;
        this.githubToken = githubToken;
        this.cloudProviderToken = cloudProviderToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGithubToken() {
        return githubToken;
    }

    public String getCloudProviderToken() {
        return cloudProviderToken;
    }


}
