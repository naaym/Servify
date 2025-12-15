package com.servify.provider.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProviderSearchResult {
    private List<ProviderSearchResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
