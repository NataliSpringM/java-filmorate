package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

// сервис для определения рейтинга фильмов
public interface FilmService {

    Film addLike(Integer id, Long userId); // добавление лайка фильму

    Film deleteLike(Integer id, Long userId); // удаление лайка у фильма

    List<Film> listMostPopularFilms(Integer count); // получение списка наиболее популярных фильмов


}
