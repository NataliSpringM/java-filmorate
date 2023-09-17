package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class ReviewDbStorage implements ReviewStorage {

    // реализация сохранения и получения информации об отзывах в базе данных
    private final JdbcTemplate jdbcTemplate;

    // добавление отзыва
    @Override
    public Review addReview(Review review) {

        // вставляем отзыв в базу данных и получаем сгенерированный id
        SimpleJdbcInsert reviewInsertion = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("review_id");

        Integer reviewId = reviewInsertion.executeAndReturnKey(review.toMap()).intValue();

        // добавляем данные отзыва, пользователя, создавшего отзыв, и нулевую оценку в таблицу с лайками и дизлайками
        String sql = "INSERT INTO reviews_likes (review_id, user_id, like_or_dislike) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, reviewId, review.getUserId(), 0);

        Review newReview = getReviewById(reviewId);

        log.info("Сохранена информация об отзыве: {}", newReview);

        return newReview;
    }

    // проверка сущестования id отзыва в базе данных
    @Override
    public void checkReviewId(Integer reviewId) {

        SqlRowSet sqlId = jdbcTemplate
                .queryForRowSet("SELECT review_id FROM reviews WHERE review_id = ?", reviewId);

        if (!sqlId.next()) {
            log.info("Отзыв с идентификатором {} не найден.", reviewId);
            throw new ObjectNotFoundException(String.format("Отзыв с id: %d не найден", reviewId));
        }
    }

    // обновление отзыва
    @Override
    public Review updateReview(Review review) {

        checkReviewId(review.getReviewId());

        String sqlQueryUpdateReview = "UPDATE reviews SET content = ?, is_positive = ? WHERE review_id = ?";

        jdbcTemplate.update(sqlQueryUpdateReview,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        Review updatedReview = getReviewById(review.getReviewId());
        log.info("Обновлена информация об отзыве: {}", updatedReview);

        return updatedReview;
    }

    // удаление отзыва
    @Override
    public void deleteReview(Integer reviewId) {

        String sqlQueryDel = "DELETE FROM reviews WHERE review_id = ?";
        jdbcTemplate.update(sqlQueryDel, reviewId);
        log.info("Удален отзыв с id {}", reviewId);

    }

    // получение списка отзывов (опции - все отзывы, отзывы по id фильма)
    @Override
    public List<Review> listReviews(Integer filmId, Integer count) {

        // получаем данные
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT r.*, rl.useful ")
                .append("FROM ")
                // выборка в зависимости от запроса
                .append((filmId == null ? "reviews r" : "(SELECT * FROM reviews WHERE film_id = " + filmId + ") r"))
                .append(" JOIN (")
                .append("SELECT review_id, ")
                .append("SUM(like_or_dislike) AS useful ")
                .append("FROM reviews_likes ")
                .append("GROUP BY review_id) rl ")
                .append("ON r.review_id = rl.review_id ")
                .append("ORDER BY rl.useful DESC ")
                .append("LIMIT ")
                .append(count);
        String sqlReview = queryBuilder.toString();

        // возвращаем список отзывов
        return jdbcTemplate.query(sqlReview, (rs, rowNum) -> new Review(
                rs.getInt("review_id"),
                rs.getString("content"),
                rs.getBoolean("is_positive"),
                rs.getLong("user_id"),
                rs.getInt("film_id"),
                rs.getInt("useful")));
    }

    // получение отзыва по идентификатору
    @Override
    public Review getReviewById(Integer reviewId) {

        checkReviewId(reviewId);

        // получаем данные
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT r.*, rl.useful")
                .append(" FROM").append(" (SELECT * FROM reviews WHERE review_id = ")
                .append(reviewId)
                .append(") r")
                .append(" JOIN")
                .append(" (SELECT review_id, SUM(like_or_dislike) AS useful")
                .append(" FROM reviews_likes")
                .append(" GROUP BY review_id) rl")
                .append(" ON r.review_id = rl.review_id");
        String sqlReview = queryBuilder.toString();
        SqlRowSet sql = jdbcTemplate.queryForRowSet(sqlReview);

        // создаем объект отзыв
        Review review;
        if (sql.next()) {
            review = new Review(
                    sql.getInt("review_id"),
                    sql.getString("content"),
                    sql.getBoolean("is_positive"),
                    sql.getLong("user_id"),
                    sql.getInt("film_id"),
                    sql.getInt("useful"));
        } else {
            log.info("Отзыв с идентификатором {} не найден.", reviewId);
            throw new ObjectNotFoundException(String.format("Отзыв с id: %d не найден", reviewId));
        }

        log.info("Найден отзыв: {}", review);
        return review;
    }

}
