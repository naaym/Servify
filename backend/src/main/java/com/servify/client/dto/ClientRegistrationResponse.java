package com.servify.client.dto;

public class ClientRegistrationResponse {
    private Long clientId;
    private String message;

    public ClientRegistrationResponse(Long clientId, String message) {
        this.clientId = clientId;
        this.message = message;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
