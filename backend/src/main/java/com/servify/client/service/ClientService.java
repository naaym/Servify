package com.servify.client.service;

import com.servify.client.dto.ClientRegistrationResponse;
import com.servify.client.dto.ClientRequest;
import com.servify.client.dto.ClientResponse;
import com.servify.client.mapper.ClientMapper;
import com.servify.client.model.Client;
import com.servify.client.repository.ClientRepository;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, ProviderRepository providerRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.providerRepository = providerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ClientResponse> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientMapper::toResponse)
                .toList();
    }

    public ClientRegistrationResponse register(ClientRequest request) {
        ensureEmailIsAvailable(request.getEmail());
        Client client = ClientMapper.toEntity(request);
        client.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        Client saved = clientRepository.save(client);
        return new ClientRegistrationResponse(saved.getId(), "Client account created successfully");
    }

    public ClientResponse findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client %d not found".formatted(id)));
        return ClientMapper.toResponse(client);
    }

    public ClientResponse create(ClientRequest request) {
        ClientRegistrationResponse registrationResponse = register(request);
        return findById(registrationResponse.getClientId());
    }

    public ClientResponse update(Long id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client %d not found".formatted(id)));

        clientRepository.findByEmail(request.getEmail())
                .filter(found -> !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Email already in use: " + found.getEmail());
                });

        providerRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email already in use: " + existing.getEmail());
                });

        ClientMapper.updateEntity(request, client);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            client.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        Client saved = clientRepository.save(client);
        return ClientMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client %d not found".formatted(id));
        }
        clientRepository.deleteById(id);
    }

    private void ensureEmailIsAvailable(String email) {
        clientRepository.findByEmail(email)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email already in use: " + existing.getEmail());
                });

        providerRepository.findByEmail(email)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email already in use: " + existing.getEmail());
                });
    }
}
