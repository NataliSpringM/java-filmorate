package ru.yandex.practicum.filmorate.exceptions;

public class UserDoesNotExistException extends RuntimeException {

    private Long id;

    public UserDoesNotExistException(String message) {
        super(message);
    }

    public UserDoesNotExistException(String message, Long id) {
        super(message);
        this.id = id;
    }

}
