package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * хранение информации о фильмах
 */

public interface FilmStorage {

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
     * сохранение новой или обновленной информации о фильме
     *
     * @param film объект Film
     */
    void updateFilmData(Film film);

    /**
     * получение списка наиболее популярных фильмов с фильтрацией по жанру и году
     *
     * @param count   возможное ограничение размера списка
     * @param genreId id жанра
     * @param year    год выхода фильма
     * @return список фильмов с возможной фильтрацией
     */

    default List<Film> listMostPopularFilms(Integer count, Integer genreId, Integer year) {
        return null;
    }

    /**
     * получение списка фильмов по режиссеру
     *
     * @param directorId id режиссера
     * @return список фильмов
     */
    default List<Film> listFilmsOfDirector(Integer directorId) {
        return null;
    }

    /**
     * поиск по названию или режиссеру
     *
     * @param substringQuery подстрока для поиска
     * @param searchBaseBy   параметры поиска
     * @return список найденных фильмов
     */
    List<Film> listSearchResults(String substringQuery, List<String> searchBaseBy);

    /**
     * проверка существования id фильма
     *
     * @param filmId id фильма
     */
    void checkFilmId(Integer filmId);

    /**
     * получение списка рекомендаций по id
     *
     * @param userId id пользователя
     * @return список рекомендаций
     */
    List<Film> getRecommendation(Long userId);

    /**
     * удаление фильма по id
     *
     * @param id id пользователя
     * @return подтверждение удаления
     */
    boolean delete(Integer id);

    /**
     * удаление всех фильмов
     */
    void clearAll();

    /**
     * получение списка общих фильмов
     *
     * @param userId   id пользователя
     * @param friendId id другого пользователя
     * @return список фильмов, понравившимся обоим пользователям
     */
    List<Film> getCommonFilmsBetweenUsers(Long userId, Long friendId);

}
