package com.servify.provider.mapper;

import com.servify.provider.dto.ProviderRegistrationRequest;
import com.servify.provider.dto.ProviderSearchResponse;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import com.servify.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public  class ProviderMapperImpl implements ProviderMapper {

  @Override
    public  ProviderEntity toEntity(ProviderRegistrationRequest request) {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setName(request.getName());
        providerEntity.setEmail(request.getEmail());
        providerEntity.setPhone(request.getPhone());
        providerEntity.setGovernorate(request.getGovernorate());
        providerEntity.setDelegation(request.getDelegation());
        providerEntity.setAge(request.getAge());
        providerEntity.setStatus(ProviderStatus.PENDING);
        providerEntity.setRole(Role.PROVIDER);
        providerEntity.setCinName(request.getCin().getOriginalFilename());
        providerEntity.setCvName(request.getCv().getOriginalFilename());
        providerEntity.setDiplomeName(request.getDiplome().getOriginalFilename());

        return providerEntity;
    }

    @Override
    public ProviderSearchResponse toSearchResponse(ProviderEntity providerEntity) {
        ProviderSearchResponse response = new ProviderSearchResponse();
        response.setId(providerEntity.getUserId());
        response.setName(providerEntity.getName());
        response.setServiceCategory(providerEntity.getServiceCategory());
        response.setGovernorate(providerEntity.getGovernorate());
        response.setDelegation(providerEntity.getDelegation());
        response.setHourlyRate(providerEntity.getHourlyRate());
        response.setAverageRating(providerEntity.getAverageRating());
        response.setReviewsCount(providerEntity.getReviewsCount());
        response.setStatus(providerEntity.getStatus());
        return response;
    }

}
