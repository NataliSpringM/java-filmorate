package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * реализация сохранения и получения информации о режиссерах в базе данных
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorMapper directorMapper;

    @Override
    public Director addDirector(Director director) {
        String sqlQuery = "INSERT INTO directors (director_name) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        return findDirectorById(keyHolder.getKey().intValue());
    }

    @Override
    public Director updateDirector(Director director) {
        int directorId = director.getId();
        String sqlQuery = "UPDATE directors SET director_name = ? WHERE director_id = ?;";
        int result = jdbcTemplate.update(sqlQuery, director.getName(), directorId);
        if (result <= 0) {
            log.info("Не выполнено обновление сведений о режиссере {}", director);
            throw new ObjectNotFoundException(
                    String.format("Обновление сведений о режиссере с id=%d не выполнено.", directorId));
        }
        return findDirectorById(directorId);
    }

    @Override
    public void deleteDirector(Integer id) {
        checkDirectorId(id);
        String sqlQuery = "DELETE FROM directors WHERE director_id = ?;";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Director findDirectorById(Integer id) {

        checkDirectorId(id);
        String sqlQuery = "SELECT * FROM directors WHERE director_id = ?;";
        Director director = jdbcTemplate.queryForObject(sqlQuery, directorMapper, id);

        log.info("Найден режиссер: {}", director);
        return director;
    }

    @Override
    public Collection<Director> findAllDirectors() {

        String sqlQuery = "SELECT * FROM directors;";
        List<Director> directors = jdbcTemplate.query(sqlQuery, directorMapper);
        logResultList(directors);
        return directors;
    }

    /**
     * получение информации о режиссерах фильма по id фильма из таблиц film_director
     * и directors
     */
    @Override
    public Set<Director> getDirectorsByFilmId(Integer filmId) {
		/*
		 * SELECT directors.* FROM (SELECT director_id AS id FROM film_directors
		 * WHERE film_id = ?)
        LEFT JOIN directors ON id = directors.director_id;
        */

        String sql = "SELECT directors.* "
                + "FROM (SELECT director_id AS id FROM film_directors WHERE film_id = ?) "
                + "LEFT JOIN directors ON id = directors.director_id;";

        List<Director> filmDirectors = jdbcTemplate.query(sql, directorMapper, filmId);

        Set<Director> directors = new TreeSet<>(Comparator.comparing(Director::getId));
        directors.addAll(filmDirectors);
        logResultList(directors);

        return directors;
    }

    /**
     * проверка существования id фильма в базе данных
     */
    @Override
    public void checkDirectorId(Integer directorId) {
        String sqlQuery = "SELECT director_id FROM directors WHERE director_id = ?;";
        SqlRowSet sqlId = jdbcTemplate.queryForRowSet(sqlQuery, directorId);

        if (!sqlId.next()) {
            log.info("Режиссер с идентификатором {} отсутствует.", directorId);
            throw new ObjectNotFoundException(String.format("Режиссер с id=%d отсутствует в хранилище.", directorId));
        }
    }

    private void logResultList(Collection<Director> directors) {

        String result = directors.stream()
                .map(Director::toString)
                .collect(Collectors.joining(", "));

        log.info("Список режиссеров по запросу: {}", result);

    }
}
