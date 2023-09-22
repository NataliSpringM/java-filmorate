package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Mpa;

/**
 * хранение информации о рейтингах фильмов
 */

public interface RatingMpaStorage {

    /**
     * получение списка рейтингов в системе MPA
     *
     * @return список рейтингов
     */
    List<Mpa> listRatingMpa();

    /**
     * получение значение рейтинга MPA по id
     *
     * @param id id рейтинга
     * @return объект Mpa
     */
    Mpa getRatingMpaById(Integer id);
}
