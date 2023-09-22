package ru.yandex.practicum.filmorate.service;

import java.util.List;

import ru.yandex.practicum.filmorate.model.FilmGenre;

/**
 * сервис для работы с жанрами
 */

public interface GenresService {

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
     * @return объект FilmGenre
     */
    FilmGenre getGenreById(Integer id);

}
