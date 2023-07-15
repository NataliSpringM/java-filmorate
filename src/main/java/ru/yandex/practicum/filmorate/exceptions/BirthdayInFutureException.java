package ru.yandex.practicum.filmorate.exceptions;

public class BirthdayInFutureException extends RuntimeException {


    public BirthdayInFutureException(String message) {
        super(message);
    }
}
