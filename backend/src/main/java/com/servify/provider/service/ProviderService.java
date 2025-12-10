package com.servify.provider.service;

import com.servify.client.repository.ClientRepository;
import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderRequest;
import com.servify.provider.dto.ProviderResponse;
import com.servify.provider.mapper.ProviderMapper;
import com.servify.provider.model.Provider;
import com.servify.provider.model.ProviderStatus;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ProviderService(ProviderRepository providerRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.providerRepository = providerRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ProviderResponse> findAll() {
        return providerRepository.findAll()
                .stream()
                .map(ProviderMapper::toResponse)
                .toList();
    }

    public ProviderRegistrationResponse register(ProviderRegistrationRequest request) {
        ensureEmailIsAvailable(request.getEmail());
        Provider provider = new Provider();
        provider.setName(request.getName());
        provider.setEmail(request.getEmail());
        provider.setPhone(request.getPhone());
        provider.setGovernorate(request.getGovernorate());
        provider.setDelegation(request.getDelegation());
        provider.setAge(request.getAge());
        provider.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        provider.setStatus(ProviderStatus.PENDING);

        provider.setCin(readFileBytes(request.getCin()));
        provider.setCv(readFileBytes(request.getCv()));
        provider.setDiplome(readFileBytes(request.getDiplome()));

        Provider saved = providerRepository.save(provider);
        return new ProviderRegistrationResponse(saved.getId(), saved.getStatus(), "Provider registration submitted for review");
    }

    public ProviderResponse findById(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider %d not found".formatted(id)));
        return ProviderMapper.toResponse(provider);
    }

    public ProviderResponse create(ProviderRequest request) {
        ensureEmailIsAvailable(request.getEmail());
        Provider provider = ProviderMapper.toEntity(request);
        provider.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        Provider saved = providerRepository.save(provider);
        return ProviderMapper.toResponse(saved);
    }

    public ProviderResponse update(Long id, ProviderRequest request) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider %d not found".formatted(id)));

        providerRepository.findByEmail(request.getEmail())
                .filter(found -> !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Email already in use: " + found.getEmail());
                });

        clientRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email already in use: " + existing.getEmail());
                });

        ProviderMapper.updateEntity(request, provider);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            provider.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        Provider saved = providerRepository.save(provider);
        return ProviderMapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Provider %d not found".formatted(id));
        }
        providerRepository.deleteById(id);
    }

    private void ensureEmailIsAvailable(String email) {
        providerRepository.findByEmail(email)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email already in use: " + existing.getEmail());
                });

        clientRepository.findByEmail(email)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email already in use: " + existing.getEmail());
                });
    }

    private byte[] readFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read uploaded file: " + file.getOriginalFilename(), ex);
        }
    }
}
