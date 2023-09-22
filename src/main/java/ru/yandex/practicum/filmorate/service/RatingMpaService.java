package ru.yandex.practicum.filmorate.service;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Mpa;

/**
 * сервис для работы с рейтингами MPA
 */
public interface RatingMpaService {

    /**
     * получение списка рейтингов MPA
     *
     * @return список рейтингов
     */
    List<Mpa> listRatingMpa();

    /**
     * получение рейтинга MPA по id
     *
     * @param id id рейтинга
     * @return объект Mpa
     */
    Mpa getRatingMPAById(Integer id);

}
