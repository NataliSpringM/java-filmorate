package ru.yandex.practicum.filmorate.storage;

/**
 * хранение информации о лайках
 */

public interface ReviewLikeStorage {

    /**
     * добавление лайка
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    void addLikeToReview(Integer reviewId, Long userId);

    /**
     * добавление дизлайка
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    void addDislikeToReview(Integer reviewId, Long userId);

    /**
     * удалить лайк у отзыва
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    void deleteLikeFromReview(Integer reviewId, Long userId);

    /**
     * удалить дизлайк у отзыва
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    void deleteDislikeFromReview(Integer reviewId, Long userId);

}
