package com.yousef.eventbooking.dto.response;

import lombok.*;

import java.util.List;

@Builder

public record BookingPageResponseDTO(
    List<BookingResponseDTO> bookings,

    Integer pageNumber,

    Integer pageSize,

    Long totalElement,

    int totalPages,

    boolean isLast
) {
}
