package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.FilmGenre;

/**
 * получение информации о жанрах фильмов
 */

public interface FilmGenreStorage {

	/**
	 * получение списка всех жанров фильмов
	 *
	 * @return
	 */
	List<FilmGenre> listFilmGenres();

	/**
	 * получение жанра по id
	 *
	 * @param id
	 * @return
	 */
	FilmGenre getGenreById(Integer id);

}
