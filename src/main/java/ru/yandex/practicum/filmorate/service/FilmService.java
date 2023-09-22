package ru.yandex.practicum.filmorate.service;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;

/**
 * сервис для определения рейтинга фильмов
 */
public interface FilmService {

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
	 * добавление лайка фильму
	 *
	 * @param id
	 * @param userId
	 */
	void addLike(Integer id, Long userId);

	/**
	 * удаление лайка у фильма
	 *
	 * @param id
	 * @param userId
	 */
	void deleteLike(Integer id, Long userId);

	/**
	 * получение списка наиболее популярных фильмов с фильтрацией по жанру и году
	 *
	 * @param count
	 * @param genreId
	 * @param year
	 * @return
	 */
	List<Film> listMostPopularFilms(Integer count, Integer genreId, Integer year);

	/**
	 * удаление фильма
	 *
	 * @param id
	 * @return
	 */
	boolean delete(Integer id);

	/**
	 * удаление всех фильмов
	 */
	void clearFilms();

	/**
	 * получение списка общих фильмов
	 *
	 * @param userId
	 * @param friendId
	 * @return
	 */
	List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId);

	/**
	 * формирование отсортированного списка фильмов режиссера с id = directorId
	 * сортировка выполняется по году выпуска фильма (sortBy="year") или по
	 * количеству лайков (sortBy="likes")
	 *
	 * @param directorId
	 * @param sortBy
	 * @return
	 */
	List<Film> listSortedFilmsOfDirector(Integer directorId, String sortBy);

	/**
	 * поиск по названию или режиссеру
	 *
	 * @param substringQuery
	 * @param searchBaseBy
	 * @return
	 */
	List<Film> listSearchResult(String substringQuery, List<String> searchBaseBy);
}
