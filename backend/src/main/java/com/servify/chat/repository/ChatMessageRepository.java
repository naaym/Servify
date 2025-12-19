package com.servify.chat.repository;

import com.servify.chat.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByBookingBookingIdOrderByCreatedAtAsc(Long bookingId);

    List<ChatMessageEntity> findTop10ByBookingClientUserIdOrderByCreatedAtDesc(Long clientId);

    List<ChatMessageEntity> findTop10ByBookingProviderUserIdOrderByCreatedAtDesc(Long providerId);
}
