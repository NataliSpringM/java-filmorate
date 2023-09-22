package ru.yandex.practicum.filmorate.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

/**
 * обработка запросов HTTP-клиентов на добавление, обновление, получение
 * информации о пользователях по адресу http://localhost:8080/users
 */
@RestController
@Validated
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final EventService eventService;

	/**
	 * обработка POST-запроса на добавление данных пользователя
	 *
	 * @param user
	 * @return
	 */
	@PostMapping()
	public User addUser(@Valid @RequestBody User user) {

		return userService.addUser(user);
	}

	/**
	 * обработка PUT-запроса на обновление данных пользователя
	 *
	 * @param user
	 * @return
	 */
	@PutMapping()
	public User updateUser(@Valid @RequestBody User user) {

		return userService.updateUser(user);
	}

	/**
	 * обработка GET-запроса на получение списка пользователей
	 *
	 * @return
	 */

	@GetMapping()
	public List<User> listUsers() {

		return userService.listUsers();
	}

	/**
	 * обработка GET-запроса на получение пользователя по id
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("{id}")
	public User getUserById(@PathVariable Long id) {

		return userService.getUserById(id);
	}

	/**
	 * обработка PUT-запроса на добавление друга
	 *
	 * @param id
	 * @param friendId
	 */
	@PutMapping("{id}/friends/{friendId}")
	public void addFriend(@RequestBody @PathVariable Long id, @PathVariable Long friendId) {

		userService.addFriend(id, friendId);
		eventService.addEvent(id, friendId, "FRIEND", "ADD");
	}

	/**
	 * обработка DELETE-запроса на добавление друга
	 *
	 * @param id
	 * @param friendId
	 */
	@DeleteMapping("{id}/friends/{friendId}")
	public void deleteFriend(@RequestBody @PathVariable Long id, @PathVariable Long friendId) {

		userService.deleteFriend(id, friendId);
		eventService.addEvent(id, friendId, "FRIEND", "REMOVE");
	}

	/**
	 * обработка GET-запроса на получение списка друзей
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("{id}/friends")
	public List<User> listUserFriends(@RequestBody @PathVariable Long id) {

		return userService.listUserFriends(id);
	}

	/**
	 * обработка GET-запроса на получение списка общих друзей
	 *
	 * @param id
	 * @param otherId
	 * @return
	 */
	@GetMapping("{id}/friends/common/{otherId}")
	public List<User> listCommonFriends(@RequestBody @PathVariable Long id, @PathVariable Long otherId) {

		return userService.listCommonFriends(id, otherId);
	}

	/**
	 * Получение списка рекомендаций
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("{id}/recommendations")
	@ResponseBody
	public List<Film> getRecommendation(@PathVariable("id") Long id) {
		return userService.getRecommendation(id);
	}

	/**
	 * обработка DELETE-запроса на добавление друга
	 *
	 * @return
	 */
	@DeleteMapping
	public List<User> deleteAll() {
		log.info("Получен запрос к эндпоинту: 'DELETE_USERS'. " + "Список пользователей пуст.");
		userService.clearAll();
		return userService.listUsers();
	}

	/**
	 * обработка DELETE-запроса на добавление друга
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/{id}")
	public boolean delete(@Valid @PathVariable Integer id) {
		log.info("Получен запрос к эндпоинту: 'DELETE_USERS_ID'.");
		boolean deleted = userService.delete(id);
		if (deleted) {
			log.debug("Возвращены данные пользователя id = {}.", id);
			return deleted;
		} else {
			log.warn("Пользователь id = {} в списке не найден.", id);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * обработка GET-запроса на получение ленты событий для пользователя
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("{id}/feed")
	public List<Event> listUserEvents(@RequestBody @PathVariable Long id) {
		return eventService.listUserEvents(id);
	}
}
