package ru.yandex.practicum.filmorate.exceptions;

/**
 * ошибка - объект не найден
 */
public class ObjectNotFoundException extends RuntimeException {

	public ObjectNotFoundException(String message) {
		super(message);
	}

}
