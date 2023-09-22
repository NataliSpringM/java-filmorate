package ru.yandex.practicum.filmorate.service;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Review;

/**
 * сервис для работы с отзывами
 */
public interface ReviewService {

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
     * @param id id отзыва
     */
    void deleteReview(Integer id);

    /**
     * получение списка отзывов
     *
     * @param filmId id фильма
     * @param count  возможное ограничение списка отзывов
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
     * добавление лайка отзыву
     *
     * @param id     id отзыва
     * @param userId id пользователя
     */
    void addLikeToReview(Integer id, Long userId);

    /**
     * добавление дизлайка отзыву
     *
     * @param id     id отзыва
     * @param userId id пользователя
     */
    void addDislikeToReview(Integer id, Long userId);

    /**
     * удаление лайка у отзыва
     *
     * @param id     id отзыва
     * @param userId id пользователя
     */
    void deleteLikeFromReview(Integer id, Long userId);

    /**
     * удаление дизлайка у отзыва
     *
     * @param id     id отзыва
     * @param userId id пользователя
     */
    void deleteDislikeFromReview(Integer id, Long userId);
}
