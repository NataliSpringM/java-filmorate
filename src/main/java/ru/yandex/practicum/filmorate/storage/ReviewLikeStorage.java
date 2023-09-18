package ru.yandex.practicum.filmorate.storage;

public interface ReviewLikeStorage {
    void addLikeToReview(Integer reviewId, Long userId); // добавление лайка

    void addDislikeToReview(Integer reviewId, Long userId); // добавление дизлайка

    void deleteLikeFromReview(Integer reviewId, Long userId); // удалить лайк у отзыва

    void deleteDislikeFromReview(Integer reviewId, Long userId); // удалить дизлайк у отзыва


}
