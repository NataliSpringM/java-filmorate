package ru.yandex.practicum.filmorate.storage;

/**
 * хранение информации о поставленных лайках
 */

public interface LikeStorage {

    /**
     * добавление лайка определенному фильму от пользователя
     *
     * @param filmId id фильма
     * @param userId id пользователя
     */
    void addLike(Integer filmId, Long userId);

    /**
     * удаление лайка у определенного фильма от пользователя
     *
     * @param filmId id фильма
     * @param userId id пользователя
     */
    void deleteLike(Integer filmId, Long userId);

    /**
     * подсчет лайков определенному фильму от всех пользователей
     *
     * @param filmId id фильма
     * @return общее количество лайков
     */
    Long getFilmLikesTotalCount(Integer filmId);

}
