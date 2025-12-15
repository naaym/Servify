package com.servify.provider.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderSearchRequest {

    @NotBlank
    private String serviceCategory;

    @NotBlank
    private String governorate;

    private Double minPrice;

    private Double maxPrice;

    private Double minRating;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

    private ProviderSortBy sortBy = ProviderSortBy.RATING_DESC;
}
