package com.servify.payment.repository;

import com.servify.payment.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findFirstByOrderIdOrderByCreatedAtDesc(Long orderId);
    Optional<PaymentTransaction> findByPaymentIntentId(String paymentIntentId);
}
