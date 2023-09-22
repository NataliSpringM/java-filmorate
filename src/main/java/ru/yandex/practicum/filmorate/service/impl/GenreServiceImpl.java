package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.GenresService;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.List;

/**
 * реализация сервиса для получения информации о жанрах фильмов
 */ 

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenresService {

    private final FilmGenreStorage filmGenreStorage;

    /**
     *  получение списка жанров
     */
    @Override
    public List<FilmGenre> listFilmGenres() {

        return filmGenreStorage.listFilmGenres();
    }

    /**
     *  получение жанра по id
     */
    @Override
    public FilmGenre getGenreById(Integer id) {

        return filmGenreStorage.getGenreById(id);
    }

}
