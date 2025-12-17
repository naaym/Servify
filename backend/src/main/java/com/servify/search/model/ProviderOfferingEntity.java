package com.servify.search.model;

import com.servify.provider.model.ProviderEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "provider_offerings")
public class ProviderOfferingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private ProviderEntity provider;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "governorate_id", nullable = false)
    private GovernorateEntity governorate;

    @Column(nullable = false)
    private boolean active = true;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;
}
