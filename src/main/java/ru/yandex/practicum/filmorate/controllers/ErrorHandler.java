package ru.yandex.practicum.filmorate.controllers;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

/**
 * обработка выбрасываемых исключений
 */
@RestControllerAdvice("ru.yandex.practicum.filmorate.controllers")
public class ErrorHandler {

	/**
	 * обработка ошибок при запросах с несуществующим идентификатором объета
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFoundId(final RuntimeException e) {

		return new ErrorResponse("Несуществующий id: " + e.getMessage());
	}

	/**
	 * обработка ошибок при прохождении валидации
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleFailValidation(final RuntimeException e) {

		return new ErrorResponse("Ошибка валидации: " + e.getMessage());
	}

	/**
	 * обработка непредвиденных ошибок
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleUnknownError(final Throwable e) {

		return new ErrorResponse("Произошла непредвиденная ошибка: " + e.getMessage());
	}

}
