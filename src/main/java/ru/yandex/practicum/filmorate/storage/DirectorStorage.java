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
	 * @param director
	 * @return
	 */
	Director addDirector(Director director);

	/**
	 * Обновление информации о режиссере.
	 *
	 * @param director
	 * @return
	 */
	Director updateDirector(Director director);

	/**
	 * Удаление информации о режиссере по заданному идентификатору.
	 *
	 * @param id
	 */
	void deleteDirector(Integer id);

	/**
	 * Получение из хранилища объекта Director по id режиссера.
	 *
	 * @param id
	 * @return
	 */
	Director findDirectorById(Integer id);

	/**
	 * Получение списка режиссеров из хранилища.
	 *
	 * @return
	 */
	Collection<Director> findAllDirectors();

	/**
	 * Получение списка режиссеров по идентификатору фильма.
	 *
	 * @param filmId
	 * @return
	 */
	Set<Director> getDirectorsByFilmId(Integer filmId);

	/**
	 * Проверка наличия идентификатора режиссера в хранилище.
	 *
	 * @param directorId
	 */
	void checkDirectorId(Integer directorId);
}
