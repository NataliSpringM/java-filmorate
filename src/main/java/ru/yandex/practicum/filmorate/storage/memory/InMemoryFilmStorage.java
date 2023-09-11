package ru.yandex.practicum.filmorate.storage.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    // реализация хранения информации о фильмах в памяти

    private final HashMap<Integer, Film> films = new HashMap<>();
    private final RatingMpaStorage mpaStorage;
    private final FilmGenreStorage filmGenreStorage;
    private Integer nextId = 1;


    // добавление информации о фильме
    @Override
    public Film addFilm(Film film) {

        Integer filmId = nextId;
        Film newFilm = film.toBuilder().id(filmId).build();
        nextId++;

        updateFilmData(newFilm);// сохранение информации о фильме
        log.info("Сохранена информация о фильме: {}", newFilm);

        return newFilm;

    }

    // проверка сущестования id фильма
    @Override
    public void checkFilmId(Integer filmId) {

        if (!films.containsKey(filmId)) {
            throw new ObjectNotFoundException(String.format("Фильм с id: %d не найден", filmId));
        }
    }

    // обновление информации о фильме
    @Override
    public Film updateFilm(Film film) {

        checkFilmId(film.getId());
        updateFilmData(film); // сохранение обновленной информации

        Film updatedFilm = getFilmById(film.getId());

        log.info("Обновлена информация о фильме: {}", updatedFilm);

        return updatedFilm;
    }

    // получение списка фильмов
    @Override
    public List<Film> listFilms() {

        List<Film> listFilms = new ArrayList<>(films.values());

        log.info("Количество фильмов в списке: {}", listFilms.size());

        return listFilms;
    }

    // получение фильма по идентификатору
    @Override
    public Film getFilmById(Integer filmId) {

        return films.values().stream()
                .filter(film -> film.getId().equals(filmId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Фильм с id: %d не найден", filmId)));
    }

    // сохранение новой или обновленной информации о фильме
    public void updateFilmData(Film film) {

        // получение информации о рейтинге фильма
        Mpa mpa = mpaStorage.getRatingMpaById(film.getMpa().getId());

        // получение информации о жанре фильма
        Set<FilmGenre> genres = (film.getGenres() == null) ?
                new TreeSet<>(Comparator.comparing(FilmGenre::getId)) : film.getGenres();

        Set<FilmGenre> fullGenres = new TreeSet<>(Comparator.comparing(FilmGenre::getId));

        for (FilmGenre genre : genres) {
            FilmGenre filmGenre = filmGenreStorage.getGenreById(genre.getId());
            fullGenres.add(filmGenre);
        }
        // обновление и сохранение информации о фильме
        Film updatedFilm = film.toBuilder().mpa(mpa).genres(fullGenres).build();

        films.put(film.getId(), updatedFilm);
    }

    // получение отсортированного списка наиболее популярных фильмов с учетом заданных ограничений
    @Override
    public List<Film> listMostPopularFilms(Integer limit) {
        return listFilms().stream()
                .sorted(Comparator.comparing(Film::getLikes, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .collect(Collectors.toList());
    }


}
