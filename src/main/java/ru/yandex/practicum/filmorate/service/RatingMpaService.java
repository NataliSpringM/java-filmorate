package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

/**
 *  сервис для работы с рейтингами MPA
 */
public interface RatingMpaService {
	
	/**
	 *  получение списка рейтингов MPA
	 * @return
	 */
    List<Mpa> listRatingMpa();

    /**
     *  получение рейтинга MPA по id
     * @param id
     * @return
     */
    Mpa getRatingMPAById(Integer id);

}
