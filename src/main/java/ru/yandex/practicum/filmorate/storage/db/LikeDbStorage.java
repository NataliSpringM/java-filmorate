package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class LikeDbStorage implements LikeStorage {

    // реализация хранения информации о лайках в базе данных
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Integer filmId, Long userId) {

        // получаем информацию о наличии лайков в базе данных
        SqlRowSet sqlLikesQuantity = jdbcTemplate.queryForRowSet(
                "SELECT * FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);

        if (sqlLikesQuantity.next()) {

            // выбрасываем исключение при попытке поставить повторный лайк
            throw new RuntimeException("Вы уже ставили лайк этому фильму");

        } else {

            // ставим лайк фильму
            String sqlQueryAddLike = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQueryAddLike, filmId, userId);

            log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);

        }

    }

    @Override
    public void deleteLike(Integer filmId, Long userId) {

        // получаем информацию о наличии лайков в базе данных
        SqlRowSet sqlLikesQuantity = jdbcTemplate.queryForRowSet(
                "SELECT * FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);

        if (!sqlLikesQuantity.next()) {

            // выбрасываем исключение при попытке удалить несуществующий лайк
            throw new RuntimeException("Вы не ставили лайк этому фильму");

        } else {
            // удаляем лайк у фильма
            String sqlQueryDeleteLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

            jdbcTemplate.update(sqlQueryDeleteLike, filmId, userId);

            log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);

        }

    }

    // подсчет лайков определенному фильму от всех пользователей
    public Long getFilmLikesTotalCount(Integer filmId) {

        SqlRowSet sqlLikes = jdbcTemplate
                .queryForRowSet("SELECT COUNT(l.user_id) as quantity FROM likes AS l WHERE film_id = ?", filmId);

        long likes = 0L;

        if (sqlLikes.next()) {
            likes = sqlLikes.getLong("quantity");
        }

        return likes;

    }
}


