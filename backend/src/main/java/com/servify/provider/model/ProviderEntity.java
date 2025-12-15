package com.servify.provider.model;

import com.servify.user.model.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "providers")
public class ProviderEntity extends UserEntity {

    @Column(nullable = false)
    private String delegation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderStatus status;
    private int age;

    @Column(nullable = false)
    private String serviceCategory;

    private Double hourlyRate;

    private Double averageRating;

    private Integer reviewsCount;


    private String cinUrl;

    private String cvUrl;

    private String diplomeUrl;
    private String cinName;
    private String cvName;
    private String diplomeName;

    @PrePersist
    void initializeDefaults() {
        if (averageRating == null) {
            averageRating = 0.0;
        }
        if (reviewsCount == null) {
            reviewsCount = 0;
        }
        if (hourlyRate == null) {
            hourlyRate = 0.0;
        }
        if (serviceCategory == null || serviceCategory.isBlank()) {
            serviceCategory = "general";
        }
        normalizeServiceCategory();
    }

    @PreUpdate
    void normalizeOnUpdate() {
        normalizeServiceCategory();
    }

    private void normalizeServiceCategory() {
        if (serviceCategory != null) {
            serviceCategory = serviceCategory.trim().toLowerCase(Locale.ROOT);
            if (serviceCategory.isBlank()) {
                serviceCategory = "general";
            }
        }
    }





}
