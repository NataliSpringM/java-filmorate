package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")

public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer nextId = 1;
    private final static int MAX_DESCRIPTION_LENGTH = 200;
    private final static LocalDate RELEASE_DATE_OLD_LIMIT = LocalDate.of(1895, 12, 28);

    @GetMapping()
    public List<Film> listFilms() {

        List<Film> listFilms = new ArrayList<>(films.values());

        log.info("Количество фильмов в списке: {}", listFilms.size());

        return listFilms;

    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {

        validateInfo(film); // проверка данных на соответствие требуемому формату

        film.setId(nextId);
        nextId++;

        updateInfo(film); // сохранение информации о фильме
        log.info("Сохранена информация о фильме: {}", film);

        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {

        if (!films.containsKey(film.getId())) {
            throw new FilmDoesNotExistException("Такого фильма нет в списке.");
        }

        validateInfo(film);

        updateInfo(film);

        log.info("Обновлена информация о фильме: {}", film);

        return film;
    }


    private void validateInfo(Film film) {

        if (isNameEmpty(film)) {
            log.error("Не прошло проверку название фильма: {}", film.getName());
            throw new NameValidationException("Название фильма не может быть пустым.");
        }
        if (isDescriptionTooLong(film)) {
            log.error("Не прошло проверку описание фильма: {},", film.getDescription());
            throw new DescriptionLengthValidationException("Описание фильма не должно превышать 200 симоволов.");
        }
        if (isReleaseDateTooOld(film)) {
            log.error("Не прошла проверку дата выхода фильма: {},", film.getReleaseDate());
            throw new ReleaseDateException("Дата выхода фильма не может быть раньше 28.12.1895 года.");
        }
        if (isDurationNotPositiveNumber(film)) {
            log.error("Не прошла проверку продолжительность фильма: {},", film.getDuration());
            throw new DurationNotPositiveValueException("Продолжительность фильма должна быть положительным числом.");
        }
        log.info("Введенные данные фильма прошли проверку.");
    }

    private void updateInfo(Film film) {
        films.put(film.getId(), film);
    }

    private boolean isNameEmpty(Film film) {
        return film.getName() == null || film.getName().isEmpty() || film.getName().isBlank();
    }

    private boolean isDescriptionTooLong(Film film) {
        return film.getDescription().length() > MAX_DESCRIPTION_LENGTH;
    }

    private boolean isReleaseDateTooOld(Film film) {
        return film.getReleaseDate().isBefore(RELEASE_DATE_OLD_LIMIT);
    }

    private boolean isDurationNotPositiveNumber(Film film) {
        return film.getDuration() <= 0;
    }


}



