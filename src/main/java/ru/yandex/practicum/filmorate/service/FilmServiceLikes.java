package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

//реализация сервиса для определения рейтинга фильмов
@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceLikes implements FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;
    private static final Integer HIT_LIST_SIZE = 10;

    // возвращение копии объекта фильм с увеличенным значением лайков
    @Override
    public Film addLike(Integer filmId, Long userId) {

        // проверяем существование id
        checkIfFilmIdExist(filmId);
        checkIfUserIdExist(userId);

        return likeStorage.addLike(filmId, userId);
    }

    // возвращение копии объекта фильм с уменьшенным значением лайков
    @Override
    public Film deleteLike(Integer filmId, Long userId) {

        // проверяем существование id
        checkIfFilmIdExist(filmId);
        checkIfUserIdExist(userId);

        return likeStorage.deleteLike(filmId, userId);
    }

    // получение списка наиболее популярных фильмов
    @Override
    public List<Film> listMostPopularFilms(Integer count) {

        // получение ограничения размера списка или его установка
        int limit = Optional.ofNullable(count).orElse(HIT_LIST_SIZE);

        // возвращение отсортированного по популярности фильмов списка определенного размера
        List<Film> mostPopularFilms = sortFilmReversedOrderWithLimit(limit);
        log.info("Количество популярных фильмов по запросу: {}", mostPopularFilms.size());

        return mostPopularFilms;
    }

    // сортировка фильмов в порядке убывания популярности с заданным ограничением размера списка
    private List<Film> sortFilmReversedOrderWithLimit(int limit) {

        return filmStorage.listFilms().stream()
                .sorted(Comparator.comparing(Film::getLikes, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // проверка наличия id пользователя
    private void checkIfUserIdExist(Long userId) {

        if (!userStorage.getUsersData().containsKey(userId)) {
            throw new UserDoesNotExistException("Пользователь с id: " + userId + " не найден.", userId);
        }
    }

    // проверка наличия id фильма
    private void checkIfFilmIdExist(Integer filmId) {

        if (!filmStorage.getFilmsData().containsKey(filmId)) {
            throw new FilmDoesNotExistException("Фильм c id: " + filmId + " не найден.", filmId);
        }
    }

}
