package ru.yandex.practicum.filmorate.storage.db;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmGenreMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

/**
 * реализация получения информации о жанрах фильмов из базы данных
 */
@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class FilmGenreDbStorage implements FilmGenreStorage {


    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreMapper filmGenreMapper;

    /**
     * получение списка жанров фильмов из базы данных
     */
    @Override
    public List<FilmGenre> listFilmGenres() {

        String sql = "SELECT * FROM genres ORDER BY genre_id";
        List<FilmGenre> genres = jdbcTemplate.query(sql,
                filmGenreMapper);
        logResultList(genres);
        return genres;

    }

    /**
     * получение информации о жанре фильма по id
     */
    @Override
    public FilmGenre getGenreById(Integer genreId) {

        checkGenreId(genreId);
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        FilmGenre filmGenre = jdbcTemplate.queryForObject(sql, filmGenreMapper, genreId);
        log.info("Найден жанр: {} {}", genreId, Objects.requireNonNull(filmGenre).getName());
        return filmGenre;

    }

    public Set<FilmGenre> getFilmGenres(Integer filmId) {

        String sql = "SELECT g.* FROM genres AS g JOIN film_genres fg ON fg.genre_id = g.genre_id WHERE fg.film_id = ?";
        List<FilmGenre> filmGenres = jdbcTemplate.query(sql, filmGenreMapper, filmId);

        Set<FilmGenre> genres = new TreeSet<>(Comparator.comparing(FilmGenre::getId));
        genres.addAll(filmGenres);
        logResultList(genres);
        return genres;
    }


    // проверка сущестования id жанра в базе данных
    @Override
    public void checkGenreId(Integer genreId) {

        SqlRowSet sql = jdbcTemplate.queryForRowSet("SELECT genre_id FROM genres WHERE genre_id = ?", genreId);
        if (!sql.next()) {
            log.info("Жанр с идентификатором {} не найден.", genreId);
            throw new ObjectNotFoundException(String.format("Жанр с id: %d не найден", genreId));
        }
    }

    /**
     * логирование списка
     */
    private void logResultList(Collection<FilmGenre> genres) {

        String result = genres.stream()
                .map(FilmGenre::toString)
                .collect(Collectors.joining(", "));
        log.info("Список жанров по запросу: {}", result);
    }
}
