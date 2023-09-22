package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *  реализация получения информации о жанрах фильмов из базы данных
 */
@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class FilmGenreDbStorage implements FilmGenreStorage {



    private final JdbcTemplate jdbcTemplate;

    /**
     *  получение списка жанров фильмов из базы данных
     */
    @Override
    public List<FilmGenre> listFilmGenres() {

        String sql = "SELECT * FROM genres ORDER BY genre_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new FilmGenre(
                rs.getInt("genre_id"),
                rs.getString("genre_name"))
        );

    }

    /**
     *  получение информации о жанре фильма по id
     */
    @Override
    public FilmGenre getGenreById(Integer genreId) {

        SqlRowSet sqlGenre = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id = ?", genreId);

        if (sqlGenre.next()) {

            FilmGenre filmGenre = new FilmGenre(
                    sqlGenre.getInt("genre_id"),
                    sqlGenre.getString("genre_name")
            );

            log.info("Найден жанр: {} {}", genreId, filmGenre.getName());
            return filmGenre;

        } else {

            log.info("Жанр с идентификатором {} не найден.", genreId);
            throw new ObjectNotFoundException(String.format("Жанр с id %d не найден", genreId));
        }
    }

    public Set<FilmGenre> getGenres(int filmId) {

        SqlRowSet genreRows = jdbcTemplate
                .queryForRowSet("SELECT genre_id FROM film_genres WHERE film_id = ?", filmId);

        Set<FilmGenre> genres = new TreeSet<>(Comparator.comparing(FilmGenre::getId));

        while (genreRows.next()) {
            // получение всей информации о жанре фильма
            FilmGenre filmGenre = getGenreById(genreRows.getInt("genre_id"));
            genres.add(filmGenre);

        }

        return genres;
    }
}
