package ru.yandex.practicum.filmorate.storage;

/**
 * хранение информации о лайках
 */

public interface ReviewLikeStorage {

	/**
	 * добавление лайка
	 *
	 * @param reviewId
	 * @param userId
	 */
	void addLikeToReview(Integer reviewId, Long userId);

	/**
	 * добавление дизлайка
	 *
	 * @param reviewId
	 * @param userId
	 */
	void addDislikeToReview(Integer reviewId, Long userId);

	/**
	 * удалить лайк у отзыва
	 *
	 * @param reviewId
	 * @param userId
	 */
	void deleteLikeFromReview(Integer reviewId, Long userId);

	/**
	 * удалить дизлайк у отзыва
	 *
	 * @param reviewId
	 * @param userId
	 */
	void deleteDislikeFromReview(Integer reviewId, Long userId);

}
