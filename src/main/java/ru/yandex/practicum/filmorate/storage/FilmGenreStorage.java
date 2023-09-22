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
     * @return список жанров
     */
    List<FilmGenre> listFilmGenres();

    /**
     * получение жанра по id
     *
     * @param id id жанра
     * @return список жанров
     */
    FilmGenre getGenreById(Integer id);

    /**
     * проверка существования жанра по id
     *
     * @param genreId id жанра
     */
    void checkGenreId(Integer genreId);
}
