package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.GenresService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
@Validated
@RequiredArgsConstructor
public class GenreController {

      /* обработка запросов HTTP-клиентов на  получение информации о жанрах фильмов по адресу
    http://localhost:8080/genres */

    private final GenresService genreService;


    // обработка GET-запроса на получение списка всех жанров
    @GetMapping()
    public List<FilmGenre> listGenres() {

        return genreService.listFilmGenres();
    }

    // обработка GET-запроса на получение жанра по id
    @GetMapping("/{id}")
    public FilmGenre getGenreById(@PathVariable Integer id) {

        return genreService.getGenreById(id);
    }

}
