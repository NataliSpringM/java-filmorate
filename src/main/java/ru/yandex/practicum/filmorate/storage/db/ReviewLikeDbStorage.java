package ru.yandex.practicum.filmorate.storage.db;

import java.util.Objects;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;

/**
 * реализация сохранения и получения информации о лайках отзывам в базе данных
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class ReviewLikeDbStorage implements ReviewLikeStorage {

    private final JdbcTemplate jdbcTemplate;

    /**
     * добавляем лайк отзыву
     */
    @Override
    public void addLikeToReview(Integer reviewId, Long userId) {

        // получаем информацию из базы данных
        SqlRowSet sqlLikes = jdbcTemplate.queryForRowSet(
                "SELECT like_or_dislike FROM reviews_likes WHERE review_id = ? AND user_id = ?", reviewId, userId);

        if (!sqlLikes.next()) { // при отсутствии записи

            // ставим лайк отзыву
            String sqlQueryAddLike = "INSERT INTO reviews_likes (review_id, user_id, like_or_dislike) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQueryAddLike, reviewId, userId, 1);

            log.info("Пользователь {} поставил лайк отзыву {}", userId, reviewId);

        } else { // при наличии записи

            if (Objects.equals(sqlLikes.getInt("like_or_dislike"), 1)) {
                // выбрасываем исключение при попытке поставить повторный лайк
                throw new RuntimeException("Вы уже ставили лайк этому отзыву");
            } else if (Objects.equals(sqlLikes.getInt("like_or_dislike"), -1)) {
                // выбрасываем исключение при двойственности позиции пользователя
                throw new RuntimeException("Вы поставили дизлайк этому же отзыву, удалите его, пожалуйста");
            }

        }
    }

    /**
     * добавляем дизлайк у отзыва
     */
    @Override
    public void addDislikeToReview(Integer reviewId, Long userId) {

        // получаем информацию из базы данных
        SqlRowSet sqlLikes = jdbcTemplate.queryForRowSet(
                "SELECT like_or_dislike FROM reviews_likes WHERE review_id = ? AND user_id = ?", reviewId, userId);

        if (!sqlLikes.next()) { // при отстутствии записи

            // ставим дизлайк отзыву
            String sqlQueryAddLike = "INSERT INTO reviews_likes (review_id, user_id, like_or_dislike) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQueryAddLike, reviewId, userId, -1);

            log.info("Пользователь {} поставил дизлайк фильму {}", userId, reviewId);

        } else { // при наличии записи в таблице

            if (Objects.equals(sqlLikes.getInt("like_or_dislike"), -1)) {
                // выбрасываем исключение при попытке поставить повторный дизлайк
                throw new RuntimeException("Вы уже ставили дизлайк этому отзыву");
            } else if (Objects.equals(sqlLikes.getInt("like_or_dislike"), 1)) {
                // выбрасываем исключение при двойственности позиции пользователя
                throw new RuntimeException("Вы поставили лайк этому же отзыву, удалите его, пожалуйста");
            }

        }
    }

    /**
     * удаляем лайк у отзыва
     */
    @Override
    public void deleteLikeFromReview(Integer reviewId, Long userId) {

        // получаем информацию из базы данных
        SqlRowSet sqlLikes = jdbcTemplate.queryForRowSet(
                "SELECT like_or_dislike FROM reviews_likes WHERE review_id = ? AND user_id = ?", reviewId, userId);

        if (sqlLikes.next()) { // при наличии записи

            if (Objects.equals(sqlLikes.getInt("like_or_dislike"), 1)) { // запись содержит лайк

                // удаляем лайк у отзыва
                String sqlQueryDeleteLike = "DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ?";
                jdbcTemplate.update(sqlQueryDeleteLike, reviewId, userId);
                log.info("Пользователь {} удалил лайк у фильма {}", userId, reviewId);

            } else { // при отсутствии записи или наличии дизалайка

                // выбрасываем исключение при попытке удалить несуществующий лайк
                throw new RuntimeException("Вы не ставили лайк этому фильму");
            }
        }
    }

    /**
     * удаляем дизлайк у отзыва
     */
    @Override
    public void deleteDislikeFromReview(Integer reviewId, Long userId) {

        // получаем информацию из базы данных
        SqlRowSet sqlLikes = jdbcTemplate.queryForRowSet(
                "SELECT like_or_dislike FROM reviews_likes WHERE review_id = ? AND user_id = ?", reviewId, userId);
        if (sqlLikes.next()) { // при наличии записи

            if (Objects.equals(sqlLikes.getInt("like_or_dislike"), -1)) { // запись содержит дизлайк

                // удаляем дизлайк у отзыва
                String sqlQueryDelLike = "DELETE FROM reviews_likes WHERE review_id = ? AND user_id = ?";
                jdbcTemplate.update(sqlQueryDelLike, reviewId, userId);
                log.info("Пользователь {} удалил лайк у фильма {}", userId, reviewId);

            } else { // при отсутствии записи или наличии лайка

                // выбрасываем исключение при попытке удалить несуществующий дизлайк
                throw new RuntimeException("Вы не ставили дизлайк этому фильму");
            }
        }
    }

}
