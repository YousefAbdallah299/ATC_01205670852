package com.yousef.eventbooking.exception.response;

import lombok.Builder;

@Builder
public record ViolationErrors(String fieldName, String message) {
}