package com.servify.shared.auth;

import com.servify.client.model.Client;
import com.servify.client.repository.ClientRepository;
import com.servify.provider.model.Provider;
import com.servify.provider.model.ProviderStatus;
import com.servify.provider.repository.ProviderRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ClientRepository clientRepository, ProviderRepository providerRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.providerRepository = providerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        return clientRepository.findByEmail(request.getEmail())
                .map(client -> authenticateClient(client, request.getPassword()))
                .orElseGet(() -> providerRepository.findByEmail(request.getEmail())
                        .map(provider -> authenticateProvider(provider, request.getPassword()))
                        .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail())));
    }

    private LoginResponse authenticateClient(Client client, String rawPassword) {
        validatePassword(rawPassword, client.getPasswordHash());
        return new LoginResponse(generateAccessToken(), client.getId(), "CLIENT", ProviderStatus.ACCEPTED, "Login successful");
    }

    private LoginResponse authenticateProvider(Provider provider, String rawPassword) {
        validatePassword(rawPassword, provider.getPasswordHash());
        String message = switch (provider.getStatus()) {
            case ACCEPTED -> "Login successful";
            case PENDING -> "Your provider application is still under review";
            case REJECTED -> "Your provider application has been rejected";
        };
        return new LoginResponse(generateAccessToken(), provider.getId(), "PROVIDER", provider.getStatus(), message);
    }

    private void validatePassword(String rawPassword, String hashedPassword) {
        if (!passwordEncoder.matches(rawPassword, hashedPassword)) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    private String generateAccessToken() {
        return UUID.randomUUID().toString();
    }
}
