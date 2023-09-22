package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Event.EventType;
import ru.yandex.practicum.filmorate.model.Event.OperationType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * обработка запросов HTTP-клиентов на добавление, обновление, получение информации о фильмах по адресуhttp://localhost:8080/films 
 */
@RestController
@Slf4j
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
public class FilmController {


    private final FilmService filmService;
    private final EventService eventService;


    /**
     *  обработка POST-запроса на добавление информации о фильме
     * @param film
     * @return
     */
    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {

        return filmService.addFilm(film);
    }

    /**
     *  обработка PUT-запроса на обновление информации о фильме
     * @param film
     * @return
     */
    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {

        return filmService.updateFilm(film);
    }

    /**
     *  обработка GET-запроса на получение фильма по идентификатору
     * @param id
     * @return
     */
    @GetMapping("/{id}")

    public Film getFilmById(@PathVariable Integer id) {

        return filmService.getFilmById(id);
    }

    /**
     *  обработка GET-запроса на получение списка всех фильмов
     * @return
     */
    @GetMapping()
    public List<Film> listFilms() {

        return filmService.listFilms();
    }

    /**
     *  обработка PUT-запроса на добавление лайка фильму
     * @param id
     * @param userId
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Long userId) {

        filmService.addLike(id, userId);
        eventService.addEvent(userId, Long.valueOf(id), "LIKE", "ADD");
    }

    /**
     *  обработка DELETE-запроса на удаление лайка фильму
     * @param id
     * @param userId
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Long userId) {

        filmService.deleteLike(id, userId);
        eventService.addEvent(userId, Long.valueOf(id), "LIKE", "REMOVE");
    }

    /**
     *  обработка GET-запроса на получение списка наиболее популярных фильмов
     * @param count
     * @return
     */
    @GetMapping("/popular")
    public List<Film> listMostPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {

        return filmService.listMostPopularFilms(count);
    }

    /**
     *  обработка запросов GET /films/director/{directorId}?sortBy=[year,likes]
     * @param directorId
     * @param sortBy
     * @return
     */
    @GetMapping("/director/{directorId}")
    public List<Film> getSortedFilmsOfDirector(@PathVariable @Positive Integer directorId,
                                               @RequestParam(required = false, defaultValue = "year") String sortBy) {
        if (!sortBy.equals("year") && !sortBy.equals("likes")) {
            sortBy = "year";
        }
        return filmService.listSortedFilmsOfDirector(directorId, sortBy);
    }

    /**
     *  обработка GET-запроса на получение общих фильмов между пользователями
     * @param userId
     * @param friendId
     * @return
     */
    @GetMapping("/common")
    public List<Film> listCommonFilms(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "friendId") Long friendId) {
        return filmService.getCommonFilmsBetweenUsers(userId, friendId);
    }

}



