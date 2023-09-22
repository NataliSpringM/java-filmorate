package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Set;

import ru.yandex.practicum.filmorate.model.Director;

/**
 * Интерфейс обеспечивает набор методов для работы с объектом Director в
 * хранилище
 */

public interface DirectorStorage {

    /**
     * Добавление информации о режиссере.
     *
     * @param director объект Director
     * @return объект Director с id
     */
    Director addDirector(Director director);

    /**
     * Обновление информации о режиссере.
     *
     * @param director объект Director
     * @return обновленный объект Director
     */
    Director updateDirector(Director director);

    /**
     * Удаление информации о режиссере по заданному идентификатору.
     *
     * @param id id режиссера
     */
    void deleteDirector(Integer id);

    /**
     * Получение из хранилища объекта Director по id режиссера.
     *
     * @param id id режиссера
     * @return объект Director
     */
    Director findDirectorById(Integer id);

    /**
     * Получение списка режиссеров из хранилища.
     *
     * @return список режиссеров
     */
    Collection<Director> findAllDirectors();

    /**
     * Получение списка режиссеров по идентификатору фильма.
     *
     * @param filmId id фильма
     * @return список режиссеров
     */
    Set<Director> getDirectorsByFilmId(Integer filmId);

    /**
     * Проверка наличия идентификатора режиссера в хранилище.
     *
     * @param directorId id режиссера
     */
    void checkDirectorId(Integer directorId);
}
