package ru.yandex.practicum.filmorate.controllers;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.GenresService;

/**
 * обработка запросов HTTP-клиентов на получение информации о жанрах фильмов по
 * адресу <a href="http://localhost:8080/genres">...</a>
 */
@RestController
@Slf4j
@RequestMapping("/genres")
@Validated
@RequiredArgsConstructor
public class GenreController {

    private final GenresService genreService;

    /**
     * обработка GET-запроса на получение списка всех жанров
     *
     * @return список жанров
     */
    @GetMapping()
    public List<FilmGenre> listGenres() {

        return genreService.listFilmGenres();
    }

    /**
     * обработка GET-запроса на получение жанра по id
     *
     * @param id id жанра
     * @return объект FilmGenre
     */
    @GetMapping("/{id}")
    public FilmGenre getGenreById(@PathVariable Integer id) {

        return genreService.getGenreById(id);
    }

}
