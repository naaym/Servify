package com.servify.provider.dto;

import com.servify.provider.model.ProviderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderSearchResponse {
    private Long id;
    private String name;
    private String serviceCategory;
    private String governorate;
    private String delegation;
    private Double hourlyRate;
    private Double averageRating;
    private Integer reviewsCount;
    private ProviderStatus status;
}
