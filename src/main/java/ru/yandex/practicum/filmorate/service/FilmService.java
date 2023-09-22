package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.List;

/**
 *  сервис для определения рейтинга фильмов
 */
public interface FilmService {

	/**
	 *  добавление информации о фильме
	 * @param film
	 * @return
	 */
    Film addFilm(Film film);

    /**
     *  обновление информации о фильме
     * @param film
     * @return
     */
    Film updateFilm(Film film);

    /**
     *  получение списка фильмов
     * @return
     */
    List<Film> listFilms(); 

    /**
     *  получение фильма по идентификатору
     * @param id
     * @return
     */
    Film getFilmById(Integer id);

    /**
     *  добавление лайка фильму
     * @param id
     * @param userId
     */
    void addLike(Integer id, Long userId);

    /**
     *  удаление лайка у фильма
     * @param id
     * @param userId
     */
    void deleteLike(Integer id, Long userId);

    /**
     *  получение списка наиболее популярных фильмов
     * @param count
     * @return
     */
    List<Film> listMostPopularFilms(Integer count);

    /** получение списка общих фильмов
     * 
     * @param userId
     * @param friendId
     * @return
     */
    List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId);

    /**
     * формирование отсортированного списка фильмов режиссера с id = directorId
     * сортировка выполняется по году выпуска фильма (sortBy="year") или по количеству лайков (sortBy="likes")
     * @param directorId
     * @param sortBy
     * @return
     */
    List<Film> listSortedFilmsOfDirector(Integer directorId, String sortBy);
}
