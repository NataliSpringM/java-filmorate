package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.List;

// сервис для определения рейтинга фильмов
public interface FilmService {

    Film addFilm(Film film);  // добавление информации о фильме

    Film updateFilm(Film film); // обновление информации о фильме

    List<Film> listFilms(); // получение списка фильмов

    Film getFilmById(Integer id); // получение фильма по идентификатору

    void addLike(Integer id, Long userId); // добавление лайка фильму

    void deleteLike(Integer id, Long userId); // удаление лайка у фильма

    List<Film> listMostPopularFilms(Integer count); // получение списка наиболее популярных фильмов


}
