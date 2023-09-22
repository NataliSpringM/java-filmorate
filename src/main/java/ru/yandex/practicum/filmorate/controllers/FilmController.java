package ru.yandex.practicum.filmorate.controllers;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;

/**
 * обработка запросов HTTP-клиентов на добавление, обновление, получение
 * информации о фильмах по адресуhttp://localhost:8080/films
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
     * обработка POST-запроса на добавление информации о фильме
     *
     * @param film объект Film
     * @return объект Film с id
     */
    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {

        return filmService.addFilm(film);
    }

    /**
     * обработка PUT-запроса на обновление информации о фильме
     *
     * @param film объект Film
     * @return обновленный объект Film
     */
    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {

        return filmService.updateFilm(film);
    }

    /**
     * обработка GET-запроса на получение фильма по идентификатору
     *
     * @param id id фильма
     * @return объект Film
     */
    @GetMapping("/{id}")

    public Film getFilmById(@PathVariable Integer id) {

        return filmService.getFilmById(id);
    }

    /**
     * обработка GET-запроса на получение списка всех фильмов
     *
     * @return список фильмов
     */
    @GetMapping()
    public List<Film> listFilms() {

        return filmService.listFilms();
    }

    /**
     * обработка PUT-запроса на добавление лайка фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Long userId) {

        filmService.addLike(id, userId);
        eventService.addEvent(userId, Long.valueOf(id), "LIKE", "ADD");
    }

    /**
     * обработка DELETE-запроса на удаление лайка фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Long userId) {

        filmService.deleteLike(id, userId);
        eventService.addEvent(userId, Long.valueOf(id), "LIKE", "REMOVE");
    }

    /**
     * обработка GET-запроса на получение списка наиболее популярных фильмов
     *
     * @param count возможное ограничение размера списка
     * @return список наиболее популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> listMostPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count,
                                           @RequestParam(required = false) Integer genreId,
                                           @RequestParam(required = false) Integer year) {

        return filmService.listMostPopularFilms(count, genreId, year);
    }

    /**
     * обработка DELETE-запроса на удаление фильма по id
     *
     * @param id id фильма
     * @return подтверждение удаления
     */
    @DeleteMapping(value = "/{id}")
    public boolean delete(@PathVariable Integer id) {
        log.info("Получен запрос к эндпоинту: 'DELETE_FILMS_ID', id:{}", id);
        boolean deleted = filmService.delete(id);
        if (deleted) {
            log.debug("Возвращены данные фильма id = {}.", id);
            return deleted;
        } else {
            log.warn("Фильм id = {} в списке не найден.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * обработка DELETE-запроса на удаление всех фильмов
     *
     * @return пустой список
     */
    @DeleteMapping
    public List<Film> deleteAll() {
        log.info("Получен запрос к эндпоинту: 'DELETE_FILMS'. "
                + "Список фильмов пуст.");
        filmService.clearFilms();
        return filmService.listFilms();
    }

    /**
     * обработка запросов GET /films/director/{directorId}?sortBy=[year,likes]
     *
     * @param directorId id режиссера
     * @param sortBy     параметр сортировки
     * @return cписок фильмов определенного режиссера с возможной сортировкой по году или популярности
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
     * поиск по подстроке названия фильма или режиссера
     *
     * @param query подстрока для запроса
     * @param by    параметр поиска по подстроке
     * @return список найденных фильмов
     */
    @GetMapping("/search")
    public List<Film> listSearchResult(@RequestParam(name = "query") String query,
                                       @RequestParam(name = "by") List<String> by) {

        return filmService.listSearchResult(query, by);
    }

    /**
     * обработка GET-запроса на получение общих фильмов между пользователями
     *
     * @param userId   id пользователя
     * @param friendId id второго пользователя
     * @return список общих понравившихся фильмов
     */
    @GetMapping("/common")
    public List<Film> listCommonFilms(@RequestParam(name = "userId") Long userId,
                                      @RequestParam(name = "friendId") Long friendId) {
        return filmService.getCommonFilmsBetweenUsers(userId, friendId);
    }


}
