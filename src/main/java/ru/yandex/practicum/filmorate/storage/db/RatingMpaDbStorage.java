package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * реализация получения информации о рейтингах фильмов из базы данных
 */
@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class RatingMpaDbStorage implements RatingMpaStorage {


    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    /**
     * получение списка возможных рейтингов отсортированных по id
     */
    @Override
    public List<Mpa> listRatingMpa() {

        String sql = "SELECT * FROM rating_mpa ORDER BY rating_mpa_id";
        List<Mpa> mpa = jdbcTemplate.query(sql,
                mpaMapper);
        logResultList(mpa);
        return mpa;

    }

    /**
     * получение рейтинга фильма по id
     */
    @Override
    public Mpa getRatingMpaById(Integer ratingMpaId) {

        checkMpaId(ratingMpaId);
        String sql = "SELECT * FROM rating_mpa WHERE rating_mpa_id = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sql, mpaMapper, ratingMpaId);
        log.info("Найден рейтинг: {}", mpa);
        return mpa;

    }


    // проверка сущестования id пользователя в базе данных
    private void checkMpaId(Integer mpaId) {
        SqlRowSet sql = jdbcTemplate
                .queryForRowSet("SELECT rating_mpa.rating_mpa_id FROM rating_mpa WHERE rating_mpa_id = ?",
                        mpaId);
        if (!sql.next()) {
            log.info("Рейтинг с идентификатором {} не найден.", mpaId);
            throw new ObjectNotFoundException(String.format("Рейтинг с id: %d не найден", mpaId));
        }
    }

    // логирование списка
    private void logResultList(List<Mpa> mpa) {

        String result = mpa.stream()
                .map(Mpa::toString)
                .collect(Collectors.joining(", "));

        log.info("Список рейтингов по запросу: {}.", result);

    }

}
