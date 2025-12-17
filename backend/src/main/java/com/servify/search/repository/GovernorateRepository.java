package com.servify.search.repository;

import com.servify.search.model.GovernorateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GovernorateRepository extends JpaRepository<GovernorateEntity, Long> {
    Optional<GovernorateEntity> findByCode(String code);
}
