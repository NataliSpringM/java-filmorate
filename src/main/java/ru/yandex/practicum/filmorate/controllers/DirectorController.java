package ru.yandex.practicum.filmorate.controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

/**
 * обработка запросов HTTP-клиентов на добавление, обновление, получение и
 * удаление информации о режиссерах по адресу http://localhost:8080/directors
 */
@RestController
@RequestMapping("/directors")
public class DirectorController {

	private final DirectorService directorService;

	@Autowired
	public DirectorController(DirectorService directorService) {
		this.directorService = directorService;
	}

	/**
	 * Запрос 'GET /directors' - список всех режиссёров.
	 *
	 * @return
	 */
	@GetMapping()
	public Collection<Director> findAllDirectors() {
		return directorService.findAllDirectors();
	}

	/**
	 * Запрос 'GET /directors/{id}' - получение режиссёра по id.
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Director findDirectorById(@PathVariable Integer id) {
		return directorService.findDirectorById(id);
	}

	/**
	 * Запрос 'POST /directors' - создание режиссёра.
	 *
	 * @param director
	 * @return
	 */
	@PostMapping()
	public Director addDirector(@Valid @RequestBody Director director) {
		return directorService.addDirector(director);
	}

	/**
	 * Запрос 'PUT /directors' - изменение режиссёра.
	 *
	 * @param director
	 * @return
	 */
	@PutMapping()
	public Director updateDirector(@Valid @RequestBody Director director) {
		return directorService.updateDirector(director);
	}

	/**
	 * Запрос 'DELETE /directors/{id}' - удаление режиссёра.
	 *
	 * @param id
	 */
	@DeleteMapping("/{id}")
	public void deleteDirector(@PathVariable Integer id) {
		directorService.deleteDirector(id);
	}

}
