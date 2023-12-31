package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

//реализация сервиса для определения рейтинга фильмов
@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;

    private static final Integer HIT_LIST_SIZE = 10;

    //добавление информации о фильме в FilmStorage
    @Override
    public Film addFilm(Film film) {

        return filmStorage.addFilm(film);
    }

    // обновление информации о фильме в FilmStorage
    @Override
    public Film updateFilm(Film film) {

        return filmStorage.updateFilm(film);
    }

    // получение списка фильмов из FilmStorage
    @Override
    public List<Film> listFilms() {

        return filmStorage.listFilms();
    }

    // получение фильма по идентификатору из FilmStorage
    @Override
    public Film getFilmById(Integer id) {

        return filmStorage.getFilmById(id);
    }

    // возвращение копии объекта фильм с увеличенным значением лайков из LikeStorage
    @Override
    public void addLike(Integer filmId, Long userId) {

        filmStorage.checkFilmId(filmId);
        userStorage.checkUserId(userId);

        likeStorage.addLike(filmId, userId);
    }

    // возвращение копии объекта фильм с уменьшенным значением лайков из LikeStorage
    @Override
    public void deleteLike(Integer filmId, Long userId) {

        filmStorage.checkFilmId(filmId);
        userStorage.checkUserId(userId);

        likeStorage.deleteLike(filmId, userId);
    }

    // получение списка наиболее популярных фильмов
    @Override
    public List<Film> listMostPopularFilms(Integer count) {

        // получение ограничения размера списка или его установка
        int limit = Optional.ofNullable(count).orElse(HIT_LIST_SIZE);

        // возвращение отсортированного по популярности фильмов списка определенного размера
        List<Film> mostPopularFilms = filmStorage.listMostPopularFilms(limit);

        log.info("Количество популярных фильмов по запросу: {}", mostPopularFilms.size());

        return mostPopularFilms;
    }


}
