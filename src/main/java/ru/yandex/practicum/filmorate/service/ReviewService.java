package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

// сервис для работы с отзывами

public interface ReviewService {
    Review addReview(Review review); // добавление отзыва

    Review updateReview(Review review); // обновление отзыва

    void deleteReview(Integer id); // удаление отзыва

    List<Review> listReviews(Integer filmId, Integer count); // получение списка отзывов

    Review getReviewById(Integer id); // получение отзыва по идентификатору

    void addLikeToReview(Integer id, Long userId); // добавление лайка отзыву

    void addDislikeToReview(Integer id, Long userId); // добавление дизлайка отзыву

    void deleteLikeFromReview(Integer id, Long userId);  // удаление лайка у отзыва

    void deleteDislikeFromReview(Integer id, Long userId); // удаление дизлайка у отзыва
}
