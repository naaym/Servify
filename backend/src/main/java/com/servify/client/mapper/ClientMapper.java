package com.servify.client.mapper;

import com.servify.client.dto.ClientRequest;
import com.servify.client.dto.ClientResponse;
import com.servify.client.model.Client;

public final class ClientMapper {

    private ClientMapper() {
    }

    public static Client toEntity(ClientRequest request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setGovernorate(request.getGovernorate());
        return client;
    }

    public static void updateEntity(ClientRequest request, Client client) {
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setGovernorate(request.getGovernorate());
    }

    public static ClientResponse toResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setName(client.getName());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        response.setGovernorate(client.getGovernorate());
        response.setCreatedAt(client.getCreatedAt());
        return response;
    }
}
