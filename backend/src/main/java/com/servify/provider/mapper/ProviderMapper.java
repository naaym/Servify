package com.servify.provider.mapper;

import com.servify.provider.dto.ProviderRequest;
import com.servify.provider.dto.ProviderResponse;
import com.servify.provider.model.Provider;

public final class ProviderMapper {

    private ProviderMapper() {
    }

    public static Provider toEntity(ProviderRequest request) {
        Provider provider = new Provider();
        provider.setName(request.getName());
        provider.setEmail(request.getEmail());
        provider.setPhone(request.getPhone());
        provider.setGovernorate(request.getGovernorate());
        provider.setDelegation(request.getDelegation());
        provider.setAge(request.getAge());
        provider.setStatus(request.getStatus());
        return provider;
    }

    public static void updateEntity(ProviderRequest request, Provider provider) {
        provider.setName(request.getName());
        provider.setEmail(request.getEmail());
        provider.setPhone(request.getPhone());
        provider.setGovernorate(request.getGovernorate());
        provider.setDelegation(request.getDelegation());
        provider.setAge(request.getAge());
        provider.setStatus(request.getStatus());
    }

    public static ProviderResponse toResponse(Provider provider) {
        ProviderResponse response = new ProviderResponse();
        response.setId(provider.getId());
        response.setName(provider.getName());
        response.setEmail(provider.getEmail());
        response.setPhone(provider.getPhone());
        response.setGovernorate(provider.getGovernorate());
        response.setDelegation(provider.getDelegation());
        response.setAge(provider.getAge());
        response.setStatus(provider.getStatus());
        response.setCreatedAt(provider.getCreatedAt());
        return response;
    }
}
