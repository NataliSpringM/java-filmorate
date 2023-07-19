package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/films")
@Validated
public class FilmController {

    /* обработка запросов HTTP-клиентов на добавление, обновление, получение информации о фильмах по адресу
    http://localhost:8080/films */

    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer nextId = 1;

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) { //добавление информации о фильме

        film.setId(nextId);
        nextId++;

        updateInfo(film); // сохранение информации о фильме
        log.info("Сохранена информация о фильме: {}", film);

        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) { //обновление информации о фильме

        if (!films.containsKey(film.getId())) {
            throw new FilmDoesNotExistException("Такого фильма нет в списке.");
        }

        updateInfo(film); // обновление информации
        log.info("Обновлена информация о фильме: {}", film);
        return film;
    }

    @GetMapping()
    public List<Film> listFilms() { // получение списка фильмов

        List<Film> listFilms = new ArrayList<>(films.values());

        log.info("Количество фильмов в списке: {}", listFilms.size());

        return listFilms;

    }

    public Map<Integer, Film> getFilmsData() { // получение данных о фильмах для тестирования
        return films;
    }

    private void updateInfo(Film film) {
        films.put(film.getId(), film);
    }

}



