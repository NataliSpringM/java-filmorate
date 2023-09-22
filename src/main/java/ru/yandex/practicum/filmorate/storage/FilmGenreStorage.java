package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 *  получение информации о жанрах фильмов
 */

public interface FilmGenreStorage {

	/**
	 *  получение списка всех жанров фильмов
	 * @return
	 */
    List<FilmGenre> listFilmGenres();

    /**
     *  получение жанра по id
     * @param id
     * @return
     */
    FilmGenre getGenreById(Integer id);

}
