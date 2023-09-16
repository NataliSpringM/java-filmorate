package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review addReview(Review review);  // добавление отзыва

    Review updateReview(Review review); // обновление отзыва

    void deleteReview(Integer reviewId); // удаление отзыва

    List<Review> listReviews(Integer filmId, Integer count); // получение списка отзывов

    Review getReviewById(Integer id); // получение отзыва по идентификатору

    void checkReviewId(Integer ReviewId); // проверка существования id отзыва

}
