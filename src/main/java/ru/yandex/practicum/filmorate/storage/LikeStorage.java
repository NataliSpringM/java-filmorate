package ru.yandex.practicum.filmorate.storage;

/**
 * хранение информации о поставленных лайках
 */

public interface LikeStorage {

	/**
	 * добавление лайка определенному фильму от пользователя
	 *
	 * @param filmId
	 * @param userId
	 */
	void addLike(Integer filmId, Long userId);

	/**
	 * удаление лайка у определенного фильма от пользователя
	 *
	 * @param filmId
	 * @param userId
	 */
	void deleteLike(Integer filmId, Long userId);

	/**
	 * подсчет лайков определенному фильму от всех пользователей
	 *
	 * @param filmId
	 * @return
	 */
	Long getFilmLikesTotalCount(Integer filmId);

}
