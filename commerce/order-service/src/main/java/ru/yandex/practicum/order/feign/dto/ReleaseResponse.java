package ru.yandex.practicum.order.feign.dto;

public record ReleaseResponse(
        boolean success,
        Integer availableQuantity,
        String message
) {
}
