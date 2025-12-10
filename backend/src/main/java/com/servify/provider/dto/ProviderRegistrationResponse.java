package com.servify.provider.dto;

import com.servify.provider.model.ProviderStatus;

public class ProviderRegistrationResponse {
    private Long providerId;
    private ProviderStatus status;
    private String message;

    public ProviderRegistrationResponse(Long providerId, ProviderStatus status, String message) {
        this.providerId = providerId;
        this.status = status;
        this.message = message;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
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
