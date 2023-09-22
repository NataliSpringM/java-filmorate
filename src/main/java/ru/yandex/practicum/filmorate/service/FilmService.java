package ru.yandex.practicum.filmorate.service;

import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;

/**
 * сервис для определения рейтинга фильмов
 */
public interface FilmService {

    /**
     * добавление информации о фильме
     *
     * @param film объект Film
     * @return объект Film с id
     */
    Film addFilm(Film film);

    /**
     * обновление информации о фильме
     *
     * @param film объект Film
     * @return обновленный объект Film
     */
    Film updateFilm(Film film);

    /**
     * получение списка фильмов
     *
     * @return список фильмов
     */
    List<Film> listFilms();

    /**
     * получение фильма по идентификатору
     *
     * @param id id фильма
     * @return объект Film
     */
    Film getFilmById(Integer id);

    /**
     * добавление лайка фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    void addLike(Integer id, Long userId);

    /**
     * удаление лайка у фильма
     *
     * @param id     id фильма
     * @param userId id пользователя
     */
    void deleteLike(Integer id, Long userId);

    /**
     * получение списка наиболее популярных фильмов с фильтрацией по жанру и году
     *
     * @param count   возможное ограничение списка фильмов
     * @param genreId id жанра
     * @param year    год выпуска фильма
     * @return список наиболее популярных фильмов с фильтрацией по жанру и/или году
     */
    List<Film> listMostPopularFilms(Integer count, Integer genreId, Integer year);

    /**
     * удаление фильма
     *
     * @param id id фильма
     * @return подтверждение удаления
     */
    boolean delete(Integer id);

    /**
     * удаление всех фильмов
     */
    void clearFilms();

    /**
     * получение списка общих фильмов
     *
     * @param userId   id пользователя
     * @param friendId id другого пользователя
     * @return список фильмов, понравившихся обоим пользователям
     */
    List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId);

    /**
     * формирование отсортированного списка фильмов режиссера с id = directorId
     * сортировка выполняется по году выпуска фильма (sortBy="year") или по
     * количеству лайков (sortBy="likes")
     *
     * @param directorId id режиссера
     * @param sortBy     параметры сортировки
     * @return список фильма, отсортированных согласно условиям
     */
    List<Film> listSortedFilmsOfDirector(Integer directorId, String sortBy);

    /**
     * поиск по названию или режиссеру
     *
     * @param substringQuery подстрока для поиска
     * @param searchBaseBy   параметры поиска
     * @return список найденных фильмов
     */
    List<Film> listSearchResult(String substringQuery, List<String> searchBaseBy);
}
