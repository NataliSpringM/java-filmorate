package ru.yandex.practicum.filmorate.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

/**
 * реализация сервиса для работы с отзывами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private static final Integer REVIEW_LIST_SIZE = 10;
	private final ReviewStorage reviewStorage;
	private final ReviewLikeStorage reviewLikeStorage;
	private final UserStorage userStorage;
	private final FilmStorage filmStorage;

	/**
	 * добавление отзыва в ReviewStorage
	 */
	@Override
	public Review addReview(Review review) {

		userStorage.checkUserId(review.getUserId());
		filmStorage.checkFilmId(review.getFilmId());

		return reviewStorage.addReview(review);
	}

	/**
	 * обновление отзыва в ReviewStorage
	 */
	@Override
	public Review updateReview(Review review) {

		userStorage.checkUserId(review.getUserId());
		filmStorage.checkFilmId(review.getFilmId());

		return reviewStorage.updateReview(review);
	}

	/**
	 * получение отзыва по идентификатору из ReviewStorage
	 */
	@Override
	public Review getReviewById(Integer reviewId) {

		return reviewStorage.getReviewById(reviewId);
	}

	/**
	 * удаление отзыва из ReviewStorage
	 */
	@Override
	public void deleteReview(Integer reviewId) {

		reviewStorage.deleteReview(reviewId);

	}

	/**
	 * получение списка фильмов из ReviewStorage
	 */
	@Override
	public List<Review> listReviews(Integer reviewId, Integer count) {

		// получение ограничения размера списка или его установка
		int limit = Optional.ofNullable(count).orElse(REVIEW_LIST_SIZE);

		// возвращение списка отзывов определенного размера
		List<Review> listReviews = reviewStorage.listReviews(reviewId, limit);

		log.info("Количество отзывов по запросу: {}", listReviews.size());

		return listReviews;
	}

	/**
	 * добавляем лайк фильму в ReviewLikeStorage
	 */
	@Override
	public void addLikeToReview(Integer reviewId, Long userId) {

		reviewStorage.checkReviewId(reviewId);
		userStorage.checkUserId(userId);

		reviewLikeStorage.addLikeToReview(reviewId, userId);
	}

	/**
	 * добавляем дизлайк фильму в ReviewLikeStorage
	 */
	@Override
	public void addDislikeToReview(Integer reviewId, Long userId) {

		reviewStorage.checkReviewId(reviewId);
		userStorage.checkUserId(userId);

		reviewLikeStorage.addDislikeToReview(reviewId, userId);

	}

	/**
	 * удаляем лайк у фильма в ReviewLikeStorage
	 */
	@Override
	public void deleteLikeFromReview(Integer reviewId, Long userId) {

		reviewStorage.checkReviewId(reviewId);
		userStorage.checkUserId(userId);

		reviewLikeStorage.deleteLikeFromReview(reviewId, userId);

	}

	/**
	 * удаляем дизлайк у фильма в ReviewLikeStorage
	 */
	@Override
	public void deleteDislikeFromReview(Integer reviewId, Long userId) {

		reviewStorage.checkReviewId(reviewId);
		userStorage.checkUserId(userId);

		reviewLikeStorage.deleteDislikeFromReview(reviewId, userId);

	}

}
