package com.servify.search.repository;

import com.servify.provider.model.ProviderStatus;
import com.servify.search.dto.OptionItemDto;
import com.servify.search.model.ProviderOfferingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProviderOfferingRepository extends JpaRepository<ProviderOfferingEntity, Long> {

    @Query("""
            SELECT new com.servify.search.dto.OptionItemDto(
                s.id,
                s.name,
                COUNT(DISTINCT p.userId)
            )
            FROM ProviderOfferingEntity po
            JOIN po.provider p
            JOIN po.service s
            WHERE po.active = true
              AND p.status = :status
            GROUP BY s.id, s.name
            ORDER BY LOWER(s.name)
            """)
    List<OptionItemDto> findServiceFacetsForApprovedProviders(@Param("status") ProviderStatus status);

    @Query("""
            SELECT new com.servify.search.dto.OptionItemDto(
                g.id,
                g.name,
                COUNT(DISTINCT p.userId)
            )
            FROM ProviderOfferingEntity po
            JOIN po.provider p
            JOIN po.governorate g
            JOIN po.service s
            WHERE po.active = true
              AND p.status = :status
              AND (:serviceId IS NULL OR s.id = :serviceId)
            GROUP BY g.id, g.name
            ORDER BY LOWER(g.name)
            """)
    List<OptionItemDto> findGovernorateFacetsForApprovedProviders(
            @Param("status") ProviderStatus status,
            @Param("serviceId") Long serviceId
    );
}
