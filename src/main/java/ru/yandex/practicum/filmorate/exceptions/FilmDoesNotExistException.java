package ru.yandex.practicum.filmorate.exceptions;

public class FilmDoesNotExistException extends RuntimeException {
    private Integer id;

    public FilmDoesNotExistException(String message) {
        super(message);
    }

    public FilmDoesNotExistException(String message, Integer id) {
        super(message);
        this.id = id;
    }
}
