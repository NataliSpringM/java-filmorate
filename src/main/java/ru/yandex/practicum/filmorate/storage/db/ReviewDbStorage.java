package ru.yandex.practicum.filmorate.storage.db;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mappers.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

/**
 * реализация сохранения и получения информации об отзывах в базе данных
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewMapper reviewMapper;

    /**
     * добавление отзыва
     */
    @Override
    public Review addReview(Review review) {

        // вставляем отзыв в базу данных и получаем сгенерированный id
        SimpleJdbcInsert reviewInsertion = new SimpleJdbcInsert(jdbcTemplate).withTableName("reviews")
                .usingGeneratedKeyColumns("review_id");

        Integer reviewId = reviewInsertion.executeAndReturnKey(review.toMap()).intValue();

        // добавляем данные отзыва, пользователя, создавшего отзыв, и нулевую оценку в
        // таблицу с лайками и дизлайками
        String sql = "INSERT INTO reviews_likes (review_id, user_id, like_or_dislike) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, reviewId, review.getUserId(), 0);

        Review newReview = getReviewById(reviewId);

        log.info("Сохранена информация об отзыве: {}", newReview);

        return newReview;
    }

    /**
     * проверка сущестования id отзыва в базе данных
     */
    @Override
    public void checkReviewId(Integer reviewId) {

        SqlRowSet sqlId = jdbcTemplate.queryForRowSet("SELECT review_id FROM reviews WHERE review_id = ?", reviewId);

        if (!sqlId.next()) {
            log.info("Отзыв с идентификатором {} не найден.", reviewId);
            throw new ObjectNotFoundException(String.format("Отзыв с id: %d не найден", reviewId));
        }
    }

    /**
     * обновление отзыва
     */
    @Override
    public Review updateReview(Review review) {

        checkReviewId(review.getReviewId());

        String sqlQueryUpdateReview = "UPDATE reviews SET content = ?, is_positive = ? WHERE review_id = ?";

        jdbcTemplate.update(sqlQueryUpdateReview, review.getContent(), review.getIsPositive(), review.getReviewId());

        Review updatedReview = getReviewById(review.getReviewId());
        log.info("Обновлена информация об отзыве: {}", updatedReview);

        return updatedReview;
    }

    /**
     * удаление отзыва
     */
    @Override
    public void deleteReview(Integer reviewId) {

        String sqlQueryDel = "DELETE FROM reviews WHERE review_id = ?";
        jdbcTemplate.update(sqlQueryDel, reviewId);
        log.info("Удален отзыв с id {}", reviewId);

    }

    /**
     * получение списка отзывов (опции - все отзывы, отзывы по id фильма)
     */
    @Override
    public List<Review> listReviews(Integer filmId, Integer count) {

        // получаем данные
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT r.*, rl.useful ").append("FROM ")
                // выборка в зависимости от запроса
                .append((filmId == null ? "reviews r" : "(SELECT * FROM reviews WHERE film_id = ?) r"))
                .append(" JOIN (").append("SELECT review_id, ").append("SUM(like_or_dislike) AS useful ")
                .append("FROM reviews_likes ").append("GROUP BY review_id) rl ")
                .append("ON r.review_id = rl.review_id ").append("ORDER BY rl.useful DESC ").append("LIMIT ")
                .append(count);
        String sql = queryBuilder.toString();

        List<Review> reviews;
        if (filmId == null) {
            reviews = jdbcTemplate.query(sql, reviewMapper);
        } else {
            reviews = jdbcTemplate.query(sql, reviewMapper, filmId);
        }
        logResultList(reviews);

        return reviews;
    }

    /**
     * получение отзыва по идентификатору
     */
    @Override
    public Review getReviewById(Integer reviewId) {

        checkReviewId(reviewId);

        // получаем данные
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT r.*, rl.useful").append(" FROM")
                .append(" (SELECT * FROM reviews WHERE review_id = ?")//  .append(reviewId)
                .append(") r")
                .append(" JOIN")
                .append(" (SELECT review_id, SUM(like_or_dislike) AS useful")
                .append(" FROM reviews_likes")
                .append(" GROUP BY review_id) rl")
                .append(" ON r.review_id = rl.review_id");
        String sqlReview = queryBuilder.toString();
        Review review = jdbcTemplate.queryForObject(sqlReview, reviewMapper, reviewId);

        log.info("Найден отзыв: {}", review);
        return review;
    }

    // логирование списка
    private void logResultList(List<Review> reviews) {

        String result = reviews.stream()
                .map(Review::toString)
                .collect(Collectors.joining(", "));

        log.info("Список отзывов по запросу: {}", result);

    }

}
