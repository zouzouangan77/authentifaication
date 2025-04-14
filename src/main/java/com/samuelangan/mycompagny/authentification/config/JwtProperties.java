package com.psu.rouen.cphbox.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long tokenValidityInSeconds;
    private long tokenValidityInSecondsRememberMe;

    // Getters et setters
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getTokenValidityInSeconds() {
        return tokenValidityInSeconds;
    }

    public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    public long getTokenValidityInSecondsRememberMe() {
        return tokenValidityInSecondsRememberMe;
    }

    public void setTokenValidityInSecondsRememberMe(long tokenValidityInSecondsRememberMe) {
        this.tokenValidityInSecondsRememberMe = tokenValidityInSecondsRememberMe;
    }
}
