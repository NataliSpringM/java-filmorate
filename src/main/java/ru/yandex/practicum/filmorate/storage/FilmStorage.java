package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 *  хранение информации о фильмах
 */

public interface FilmStorage {

	/**
	 * добавление информации о фильме
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
     *  сохранение новой или обновленной информации о фильме
     * @param film
     */
    void updateFilmData(Film film);

    /**
     *  получение списка наиболее популярных фильмов
     * @param count
     * @return
     */
    List<Film> listMostPopularFilms(Integer count);

    /**
     *  получение списка фильмов по режиссеру
     * @param directorId
     * @return
     */
    default List<Film> listFilmsOfDirector(Integer directorId) {
        return null;
    }

    /**
     *  проверка существования id фильма
     * @param filmId
     */
    void checkFilmId(Integer filmId);

    /**
     *  получение списка общих фильмов
     * @param userId
     * @param friendId
     * @return
     */
    List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId);

}
