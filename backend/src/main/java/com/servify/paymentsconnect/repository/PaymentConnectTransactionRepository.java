package com.servify.paymentsconnect.repository;

import com.servify.paymentsconnect.model.PaymentConnectTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentConnectTransactionRepository extends JpaRepository<PaymentConnectTransaction, Long> {
    Optional<PaymentConnectTransaction> findByPaymentIntentId(String paymentIntentId);
}
