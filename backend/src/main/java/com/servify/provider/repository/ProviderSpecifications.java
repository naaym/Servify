package com.servify.provider.repository;

import com.servify.provider.dto.ProviderSearchRequest;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.model.ProviderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ProviderSpecifications {

    private ProviderSpecifications() {
    }

    public static Specification<ProviderEntity> fromRequest(ProviderSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("status"), ProviderStatus.ACCEPTED));

            String serviceCategory = request.getServiceCategory();
            if (serviceCategory != null && !serviceCategory.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("serviceCategory")),
                        "%" + serviceCategory.trim().toLowerCase(Locale.ROOT) + "%"
                ));
            }

            String governorate = request.getGovernorate();
            if (governorate != null && !governorate.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("governorate")),
                        governorate.trim().toLowerCase(Locale.ROOT)
                ));
            }

            if (request.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("hourlyRate"), request.getMinPrice()));
            }

            if (request.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("hourlyRate"), request.getMaxPrice()));
            }

            if (request.getMinRating() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("averageRating"), request.getMinRating()));
            }

            return criteriaBuilder.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }
}
