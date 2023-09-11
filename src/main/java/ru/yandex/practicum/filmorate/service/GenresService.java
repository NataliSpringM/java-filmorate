package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

// сервис для работы с жанрами

public interface GenresService {

    List<FilmGenre> listFilmGenres(); // получение списка всех жанров фильмов

    FilmGenre getGenreById(Integer id); // получение жанра по id


}
