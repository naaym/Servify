package com.servify.search.service;

import com.servify.provider.model.ProviderStatus;
import com.servify.search.dto.OptionItemDto;
import com.servify.search.repository.ProviderOfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchOptionsService {

    private final ProviderOfferingRepository providerOfferingRepository;

    public List<OptionItemDto> getAvailableServices() {
        return providerOfferingRepository.findServiceFacetsForApprovedProviders(ProviderStatus.ACCEPTED);
    }

    public List<OptionItemDto> getAvailableGovernorates(Long serviceId) {
        return providerOfferingRepository.findGovernorateFacetsForApprovedProviders(ProviderStatus.ACCEPTED, serviceId);
    }
}
