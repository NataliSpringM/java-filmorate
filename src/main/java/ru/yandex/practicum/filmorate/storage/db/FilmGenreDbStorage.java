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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class FilmGenreDbStorage implements FilmGenreStorage {

    // реализация получения информации о жанрах фильмов из базы данных

    private final JdbcTemplate jdbcTemplate;

    // получение списка жанров фильмов из базы данных
    @Override
    public List<FilmGenre> listFilmGenres() {

        String sql = "SELECT * FROM genres ORDER BY genre_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new FilmGenre(
                rs.getInt("genre_id"),
                rs.getString("genre_name"))
        );

    }

    // получение информации о жанре фильма по id
    @Override
    public FilmGenre getGenreById(Integer genreId) {

        SqlRowSet sqlGenre = jdbcTemplate.queryForRowSet("SELECT * FROM genres where genre_id = ?", genreId);

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
}
