package com.servify.provider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderSearchRequest {
    private String serviceCategory;
    private String governorate;
    private String delegation;
    private Double minPrice;
    private Double maxPrice;
    private Double minRating;
    private String sortBy;
}
