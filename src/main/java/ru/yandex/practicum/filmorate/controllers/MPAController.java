package ru.yandex.practicum.filmorate.controllers;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

/**
 * обработка запросов HTTP-клиентов на получение информации о рейтингах фильмов
 * по адресу http://localhost:8080/mpa
 */
@RestController
@Slf4j
@RequestMapping("/mpa")
@Validated
@RequiredArgsConstructor
public class MPAController {

	private final RatingMpaService mpaService;

	/**
	 * обработка GET-запроса на получение списка всех рейтингов
	 *
	 * @return
	 */
	@GetMapping()
	public List<Mpa> listRatingMPA() {

		return mpaService.listRatingMpa();
	}

	/**
	 * обработка GET-запроса на получение рейтинга по id
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Mpa getGenreById(@PathVariable Integer id) {

		return mpaService.getRatingMPAById(id);
	}

}
