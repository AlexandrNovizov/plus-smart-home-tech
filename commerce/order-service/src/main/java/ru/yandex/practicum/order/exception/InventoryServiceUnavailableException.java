package ru.yandex.practicum.order.exception;

public class InventoryServiceUnavailableException extends RuntimeException {
    public InventoryServiceUnavailableException(String message) {
        super(message);
    }

    public InventoryServiceUnavailableException(Long productId, Throwable cause) {
        super("inventory-service недоступен при запросе товара id=" + productId, cause);
    }
}
