package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Review;

/**
 * хранение информации об отзывах
 */
@Repository
public interface ReviewStorage {

    /**
     * добавление отзыва
     *
     * @param review объект Review
     * @return объект Review с id
     */
    Review addReview(Review review);

    /**
     * обновление отзыва
     *
     * @param review объект Review
     * @return обновленный объект Review
     */
    Review updateReview(Review review);

    /**
     * удаление отзыва
     *
     * @param reviewId id отзыва
     */
    void deleteReview(Integer reviewId);

    /**
     * получение списка отзывов
     *
     * @param filmId id фильма
     * @param count  возможное ограничение количества отзывов в списке
     * @return список отзывов
     */
    List<Review> listReviews(Integer filmId, Integer count);

    /**
     * получение отзыва по идентификатору
     *
     * @param id id отзыва
     * @return объект Review
     */
    Review getReviewById(Integer id);

    /**
     * проверка существования id отзыва
     *
     * @param reviewId id отзыва
     */
    void checkReviewId(Integer reviewId);


}
