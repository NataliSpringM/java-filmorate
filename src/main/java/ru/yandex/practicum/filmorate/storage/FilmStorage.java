package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;

/**
 * хранение информации о фильмах
 */

public interface FilmStorage {

	/**
	 * добавление информации о фильме
	 *
	 * @param film
	 * @return
	 */
	Film addFilm(Film film);

	/**
	 * обновление информации о фильме
	 *
	 * @param film
	 * @return
	 */
	Film updateFilm(Film film);

	/**
	 * получение списка фильмов
	 *
	 * @return
	 */
	List<Film> listFilms();

	/**
	 * получение фильма по идентификатору
	 *
	 * @param id
	 * @return
	 */
	Film getFilmById(Integer id);

	/**
	 * сохранение новой или обновленной информации о фильме
	 *
	 * @param film
	 */
	void updateFilmData(Film film);

	/**
	 * получение списка наиболее популярных фильмов с фильтрацией по жанру и году
	 *
	 * @param count
	 * @param genreId
	 * @param year
	 * @return
	 */

	default List<Film> listMostPopularFilms(Integer count, Integer genreId, Integer year) {
		return null;
	}

	/**
	 * получение списка фильмов по режиссеру
	 *
	 * @param directorId
	 * @return
	 */
	default List<Film> listFilmsOfDirector(Integer directorId) {
		return null;
	}

	/**
	 * поиск по названию или режиссеру
	 *
	 * @param substringQuery
	 * @param searchBaseBy
	 * @return
	 */
	List<Film> listSearchResults(String substringQuery, List<String> searchBaseBy);

	/**
	 * проверка существования id фильма
	 *
	 * @param filmId
	 */
	void checkFilmId(Integer filmId);

	/**
	 * удаление фильма по id
	 *
	 * @param userId
	 * @return
	 */
	List<Film> getRecommendation(Long userId);

	/**
	 * удаление фильма по id
	 *
	 * @param id
	 * @return
	 */
	boolean delete(Integer id);

	/**
	 * удаление всех фильмов
	 */
	void clearAll();

	/**
	 * получение списка общих фильмов
	 *
	 * @param userId
	 * @param friendId
	 * @return
	 */
	List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId);

}
