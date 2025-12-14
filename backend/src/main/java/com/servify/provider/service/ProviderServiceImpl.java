package com.servify.provider.service;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;

import com.servify.provider.exceptions.EmailDuplicationException;
import com.servify.provider.mapper.ProviderMapper;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService{

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final PasswordEncoder passwordEncoder ;
    private  final StorageFilesService storageFilesService;



    public ProviderRegistrationResponse register(ProviderRegistrationRequest request) {
        ensureEmailIsAvailable(request.getEmail());
        ProviderEntity savedEntity = providerMapper.toEntity(request);
        savedEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        savedEntity.setCinUrl(storageFilesService.store(request.getCin(),"servify/providers/Cin"));
        savedEntity.setCvUrl(storageFilesService.store(request.getCv(),"servify/providers/Cv"));
        savedEntity.setDiplomeUrl(storageFilesService.store(request.getDiplome(),"servify/providers/Diplome"));

        ProviderEntity saved = providerRepository.save(savedEntity);
        return new ProviderRegistrationResponse(saved.getUserId(), saved.getStatus());
    }

    private void ensureEmailIsAvailable(String email) {
        if(providerRepository.existsByEmail(email)){
          throw new EmailDuplicationException("Email "+email+" exists" );

    }}


}
