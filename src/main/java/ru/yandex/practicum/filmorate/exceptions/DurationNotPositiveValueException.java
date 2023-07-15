package ru.yandex.practicum.filmorate.exceptions;

public class DurationNotPositiveValueException extends RuntimeException {
    public DurationNotPositiveValueException(String message) {
        super(message);
    }
}
