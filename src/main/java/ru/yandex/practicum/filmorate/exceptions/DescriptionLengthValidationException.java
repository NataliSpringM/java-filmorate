package ru.yandex.practicum.filmorate.exceptions;

public class DescriptionLengthValidationException extends RuntimeException {

    public DescriptionLengthValidationException(String message) {
        super(message);
    }
}
