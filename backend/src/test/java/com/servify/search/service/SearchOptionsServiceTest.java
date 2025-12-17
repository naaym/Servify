package com.servify.search.service;

import com.servify.provider.model.ProviderStatus;
import com.servify.search.dto.OptionItemDto;
import com.servify.search.repository.ProviderOfferingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchOptionsServiceTest {

    @Mock
    private ProviderOfferingRepository providerOfferingRepository;

    private SearchOptionsService searchOptionsService;

    @BeforeEach
    void setUp() {
        searchOptionsService = new SearchOptionsService(providerOfferingRepository);
    }

    @Test
    void shouldReturnServiceFacetsForApprovedProviders() {
        List<OptionItemDto> expected = List.of(new OptionItemDto(1L, "Cleaning", 3));
        when(providerOfferingRepository.findServiceFacetsForApprovedProviders(ProviderStatus.ACCEPTED)).thenReturn(expected);

        List<OptionItemDto> result = searchOptionsService.getAvailableServices();

        assertThat(result).containsExactlyElementsOf(expected);
        verify(providerOfferingRepository).findServiceFacetsForApprovedProviders(ProviderStatus.ACCEPTED);
    }

    @Test
    void shouldReturnGovernorateFacetsFilteredByService() {
        Long serviceId = 7L;
        List<OptionItemDto> expected = List.of(new OptionItemDto(2L, "Tunis", 5));
        when(providerOfferingRepository.findGovernorateFacetsForApprovedProviders(ProviderStatus.ACCEPTED, serviceId))
                .thenReturn(expected);

        List<OptionItemDto> result = searchOptionsService.getAvailableGovernorates(serviceId);

        assertThat(result).isEqualTo(expected);

        ArgumentCaptor<Long> serviceCaptor = ArgumentCaptor.forClass(Long.class);
        verify(providerOfferingRepository)
                .findGovernorateFacetsForApprovedProviders(ProviderStatus.ACCEPTED, serviceCaptor.capture());
        assertThat(serviceCaptor.getValue()).isEqualTo(serviceId);
    }
}
