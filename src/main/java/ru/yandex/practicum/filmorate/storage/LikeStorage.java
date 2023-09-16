package ru.yandex.practicum.filmorate.storage;

// хранение информации о поставленных лайках

public interface LikeStorage {

    void addLike(Integer filmId, Long userId); // добавление лайка определенному фильму от пользователя

    void deleteLike(Integer filmId, Long userId); // удаление лайка у определенного фильма от пользователя

    Long getFilmLikesTotalCount(Integer filmId); // подсчет лайков определенному фильму от всех пользователей

}
