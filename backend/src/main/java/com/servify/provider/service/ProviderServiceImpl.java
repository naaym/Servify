package com.servify.provider.service;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderRegistrationResponse;
import com.servify.provider.dto.ProviderSearchRequest;
import com.servify.provider.dto.ProviderSearchResult;
import com.servify.provider.dto.ProviderSortBy;

import com.servify.provider.exceptions.EmailDuplicationException;
import com.servify.provider.mapper.ProviderMapper;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.repository.ProviderRepository;
import com.servify.provider.repository.ProviderSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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

    @Override
    @Transactional(readOnly = true)
    public ProviderSearchResult searchProviders(ProviderSearchRequest request) {
        log.info("Searching providers with filters: serviceCategory={}, governorate={}, minPrice={}, maxPrice={}, minRating={}, page={}, size={}, sortBy={}",
                request.getServiceCategory(), request.getGovernorate(), request.getMinPrice(), request.getMaxPrice(), request.getMinRating(), request.getPage(), request.getSize(), request.getSortBy());

        if (request.getMinPrice() != null && request.getMaxPrice() != null && request.getMinPrice() > request.getMaxPrice()) {
            log.warn("Swapping minPrice and maxPrice because minPrice > maxPrice");
            Double originalMin = request.getMinPrice();
            request.setMinPrice(request.getMaxPrice());
            request.setMaxPrice(originalMin);
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), resolveSort(request.getSortBy()));
        Page<ProviderEntity> page = providerRepository.findAll(ProviderSpecifications.fromRequest(request), pageable);

        ProviderSearchResult result = new ProviderSearchResult();
        result.setContent(page.stream().map(providerMapper::toSearchResponse).toList());
        result.setPage(page.getNumber());
        result.setSize(page.getSize());
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        return result;
    }

    private Sort resolveSort(ProviderSortBy sortBy) {
        if (sortBy == null) {
            return Sort.by(Sort.Direction.DESC, "averageRating");
        }
        return switch (sortBy) {
            case PRICE_ASC -> Sort.by(Sort.Direction.ASC, "hourlyRate");
            case PRICE_DESC -> Sort.by(Sort.Direction.DESC, "hourlyRate");
            case REVIEWS_DESC -> Sort.by(Sort.Direction.DESC, "reviewsCount");
            case RATING_DESC -> Sort.by(Sort.Direction.DESC, "averageRating");
        };
    }

    private void ensureEmailIsAvailable(String email) {
        if(providerRepository.existsByEmail(email)){
          throw new EmailDuplicationException("Email "+email+" exists" );

    }}


}
