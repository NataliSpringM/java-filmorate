package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

/**
 *  сервис для работы с отзывами
 */
public interface ReviewService {

	/**
	 *  добавление отзыва
	 * @param review
	 * @return
	 */
    Review addReview(Review review);

    /**
     *  обновление отзыва
     * @param review
     * @return
     */
    Review updateReview(Review review);

    /**
     *  удаление отзыва
     * @param id
     */
    void deleteReview(Integer id);

    /**
     *  получение списка отзывов
     * @param filmId
     * @param count
     * @return
     */
    List<Review> listReviews(Integer filmId, Integer count);

    /**
     *  получение отзыва по идентификатору
     * @param id
     * @return
     */
    Review getReviewById(Integer id);

    /**
     *  добавление лайка отзыву
     * @param id
     * @param userId
     */
    void addLikeToReview(Integer id, Long userId);

    /**
     *  добавление дизлайка отзыву
     * @param id
     * @param userId
     */
    void addDislikeToReview(Integer id, Long userId);

    /**
     *  удаление лайка у отзыва
     * @param id
     * @param userId
     */
    void deleteLikeFromReview(Integer id, Long userId);

    /**
     *  удаление дизлайка у отзыва
     * @param id
     * @param userId
     */
    void deleteDislikeFromReview(Integer id, Long userId);
}
