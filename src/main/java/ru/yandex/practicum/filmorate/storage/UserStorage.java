package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.User;

/**
 * хранение информации о пользователях
 */

public interface UserStorage {

	/**
	 * добавление информации о пользователе
	 *
	 * @param user
	 * @return
	 */
	User addUser(User user);

	/**
	 * обновление информации о пользователе
	 *
	 * @param user
	 * @return
	 */
	User updateUser(User user);

	/**
	 * получение списка пользователей
	 *
	 * @return
	 */
	List<User> listUsers();

	/**
	 * получение пользователя по идентификатору
	 *
	 * @param id
	 * @return
	 */
	User getUserById(Long id);

	/**
	 * сохранение новой или обновленной информации о пользователе
	 *
	 * @param user
	 */
	void updateUserProperties(User user);

	/**
	 * проверка существования id пользователя
	 *
	 * @param userId
	 */
	void checkUserId(Long userId);

	void clearAll(); // удаление всех пользователей

	boolean delete(Integer id); // удаление пользователя по id

}
