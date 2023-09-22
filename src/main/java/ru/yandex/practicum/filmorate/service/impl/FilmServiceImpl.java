package ru.yandex.practicum.filmorate.service.impl;

import static java.util.Comparator.comparing;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

/**
 * реализация сервиса для определения рейтинга фильмов
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

	private static final Integer HIT_LIST_SIZE = 10;
	private final FilmStorage filmStorage;
	private final LikeStorage likeStorage;
	private final UserStorage userStorage;

	/**
	 * добавление информации о фильме в FilmStorage
	 */
	@Override
	public Film addFilm(Film film) {

		return filmStorage.addFilm(film);
	}

	/**
	 * обновление информации о фильме в FilmStorage
	 */
	@Override
	public Film updateFilm(Film film) {

		return filmStorage.updateFilm(film);
	}

	/**
	 * получение списка фильмов из FilmStorage
	 */
	@Override
	public List<Film> listFilms() {

		return filmStorage.listFilms();
	}

	/**
	 * получение фильма по идентификатору из FilmStorage
	 */
	@Override
	public Film getFilmById(Integer id) {

		return filmStorage.getFilmById(id);
	}

	/**
	 * добавляем лайк фильму
	 */
	@Override
	public void addLike(Integer filmId, Long userId) {

		filmStorage.checkFilmId(filmId);
		userStorage.checkUserId(userId);

		likeStorage.addLike(filmId, userId);
	}

	/**
	 * удаляем лайк у фильма
	 */
	@Override
	public void deleteLike(Integer filmId, Long userId) {

		filmStorage.checkFilmId(filmId);
		userStorage.checkUserId(userId);

		likeStorage.deleteLike(filmId, userId);
	}

	/**
	 * получение списка наиболее популярных фильмов
	 */
	@Override
	public List<Film> listMostPopularFilms(Integer count, Integer genreId, Integer year) {
		List<Film> mostPopularFilms;

		// получение ограничения размера списка или его установка
		int limit = Optional.ofNullable(count).orElse(HIT_LIST_SIZE);

		// возвращение отсортированного по популярности фильмов списка определенного
		// размера
		mostPopularFilms = filmStorage.listMostPopularFilms(limit, genreId, year);

		log.info("Количество популярных фильмов по запросу: {}", mostPopularFilms.size());

		return mostPopularFilms;
	}

	/**
	 * Удаление фильма по id
	 */
	@Override
	public boolean delete(Integer id) {
		return filmStorage.delete(id);
	}

	/**
	 * удаление всех фильмов
	 */
	@Override
	public void clearFilms() {
		filmStorage.clearAll();
	}

	/**
	 * получение списка фильмов режиссера
	 */
	@Override
	public List<Film> listSortedFilmsOfDirector(Integer directorId, String sortBy) {
		List<Film> films = filmStorage.listFilmsOfDirector(directorId);
		if (sortBy.equals("year")) {
			return films.stream().sorted(comparing(Film::getReleaseDate)).collect(Collectors.toList());
		} else {
			return films.stream().sorted(comparing(Film::getLikes)).collect(Collectors.toList());
		}
	}

	/**
	 * получение списка общих фильмов
	 */
	@Override
	public List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId) {
		return filmStorage.getCommonFilmsBetweenUsers(userId, friendId);
	}

	// поиск по названию или режиссеру
	@Override
	public List<Film> listSearchResult(String substringQuery, List<String> searchBaseBy) {

		return filmStorage.listSearchResults(substringQuery, searchBaseBy);

	}

}
