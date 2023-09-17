package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Set;

// Интерфейс обеспечивает набор методов для работы с объектом Director в хранилище
public interface DirectorStorage {
    Director addDirector(Director director);    // Добавление информации о режиссере.

    Director updateDirector(Director director); // Обновление информации о режиссере.

    void deleteDirector(Integer id);            // Удаление информации о режиссере по заданному идентификатору.

    Director findDirectorById(Integer id);      // Получение из хранилища объекта Director по id режиссера.

    Collection<Director> findAllDirectors();    // Получение списка режиссеров из хранилища.

    Set<Director> getDirectorsByFilmId(Integer filmId); // Получение списка режиссеров по идентификатору фильма.

    void checkDirectorId(Integer directorId); // Проверка наличия идентификатора режиссера в хранилище.
}
