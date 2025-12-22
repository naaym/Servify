package com.servify.paymentsconnect.repository;

import com.servify.paymentsconnect.model.ProviderStripeAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderStripeAccountRepository extends JpaRepository<ProviderStripeAccount, Long> {
    Optional<ProviderStripeAccount> findByProvider_UserId(Long providerId);
}
