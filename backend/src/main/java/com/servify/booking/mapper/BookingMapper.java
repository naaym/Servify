package com.servify.booking.mapper;

import com.servify.booking.dto.BookingRequest;
import com.servify.booking.dto.BookingResponse;
import com.servify.booking.model.BookingEntity;
import com.servify.booking.model.BookingStatus;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingEntity toEntity(BookingRequest request) {
        BookingEntity entity = new BookingEntity();
        entity.setProviderId(request.getProviderId());
        entity.setServiceCategory(request.getServiceCategory());
        entity.setDate(request.getDate());
        entity.setTime(request.getTime());
        entity.setDescription(request.getDescription());
        entity.setStatus(BookingStatus.PENDING);
        entity.setAttachments(request.getAttachments());
        return entity;
    }

    public BookingResponse toResponse(BookingEntity entity) {
        return BookingResponse.builder()
            .id(entity.getId())
            .providerId(entity.getProviderId())
            .clientId(entity.getClientId())
            .serviceCategory(entity.getServiceCategory())
            .date(entity.getDate())
            .time(entity.getTime())
            .description(entity.getDescription())
            .status(entity.getStatus())
            .attachments(entity.getAttachments())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
