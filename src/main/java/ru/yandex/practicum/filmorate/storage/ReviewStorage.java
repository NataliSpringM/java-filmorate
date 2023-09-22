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
	 * @param review
	 * @return
	 */
	Review addReview(Review review);

	/**
	 * обновление отзыва
	 *
	 * @param review
	 * @return
	 */
	Review updateReview(Review review);

	/**
	 * удаление отзыва
	 *
	 * @param reviewId
	 */
	void deleteReview(Integer reviewId);

	/**
	 * получение списка отзывов
	 *
	 * @param filmId
	 * @param count
	 * @return
	 */
	List<Review> listReviews(Integer filmId, Integer count);

	/**
	 * получение отзыва по идентификатору
	 *
	 * @param id
	 * @return
	 */
	Review getReviewById(Integer id);

	/**
	 * проверка существования id отзыва
	 *
	 * @param reviewId
	 */
	void checkReviewId(Integer reviewId);

}
