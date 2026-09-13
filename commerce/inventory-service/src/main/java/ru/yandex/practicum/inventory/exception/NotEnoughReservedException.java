package ru.yandex.practicum.inventory.exception;

public class NotEnoughReservedException extends RuntimeException {
    public NotEnoughReservedException(String message) {
        super(message);
    }
}
