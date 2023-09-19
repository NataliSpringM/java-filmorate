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

    // получение списка наиболее популярных фильмов с фильтрацией по жанру и году
    List<Film> listMostPopularFilms(Integer count, Integer genreId, Integer year);

    // формирование отсортированного списка фильмов режиссера с id = directorId
    // сортировка выполняется по году выпуска фильма (sortBy="year") или по количеству лайков (sortBy="likes")
    List<Film> listSortedFilmsOfDirector(Integer directorId, String sortBy);
}
