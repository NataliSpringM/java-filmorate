package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class RatingMpaDbStorage implements RatingMpaStorage {

    // реализация получения информации о рейтингах фильмов из базы данных

    private final JdbcTemplate jdbcTemplate;

    // получение списка возможных рейтингов отсортированных по id
    @Override
    public List<Mpa> listRatingMpa() {

        String sql = "SELECT * FROM rating_mpa ORDER BY rating_mpa_id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(
                rs.getInt("rating_mpa_id"),
                rs.getString("rating_mpa_name")));

    }

    // получение рейтинга фильма по id
    @Override
    public Mpa getRatingMpaById(Integer ratingMpaId) {

        SqlRowSet sqlRatingQuery = jdbcTemplate
                .queryForRowSet("SELECT * FROM rating_mpa WHERE rating_mpa_id = ?", ratingMpaId);

        if (sqlRatingQuery.next()) {

            Mpa mpa = new Mpa(
                    sqlRatingQuery.getInt("rating_mpa_id"),
                    sqlRatingQuery.getString("rating_mpa_name"));
            log.info("Найден рейтинг: {}", mpa);
            return mpa;

        } else {

            log.info("Рейтинг с идентификатором {} не найден.", ratingMpaId);
            throw new ObjectNotFoundException(String.format("Рейтинг с id: %d не найден", ratingMpaId));

        }
    }

}
