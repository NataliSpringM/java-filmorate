package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

// получение информации о жанрах фильмов
public interface FilmGenreStorage {

    List<FilmGenre> listFilmGenres(); // получение списка всех жанров фильмов

    FilmGenre getGenreById(Integer id); // получение жанра по id

}
