package com.servify.shared.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.servify.provider.model.ProviderStatus;

public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private Long id;
    private String role;
    private ProviderStatus status;
    private String message;

    public LoginResponse(String accessToken, Long id, String role, ProviderStatus status, String message) {
        this.accessToken = accessToken;
        this.id = id;
        this.role = role;
        this.status = status;
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ProviderStatus getStatus() {
        return status;
    }

    public void setStatus(ProviderStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
