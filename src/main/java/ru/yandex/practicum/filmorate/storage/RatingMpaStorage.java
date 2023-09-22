package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import org.springframework.stereotype.Repository;
/**
 *  хранение информации о рейтингах фильмов
 */

public interface RatingMpaStorage {

	/**
	 *  получение списка рейтингов в системе MPA
	 * @return
	 */
    List<Mpa> listRatingMpa();

    /**
     *  получение значение рейтинга MPA по id
     * @param id
     * @return
     */
    Mpa getRatingMpaById(Integer id);
}
