package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.db.DirectorDbStorage;

/**
 * сервис для обработки данных о режиссерах
 */
@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    /**
     * Конструктор сервиса
     *
     * @param directorStorage хранилище режиссеры
     */
    @Autowired
    public DirectorService(DirectorDbStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    /**
     * Возврат списка всех режиссеров
     *
     * @return список режиссеров
     */
    public Collection<Director> findAllDirectors() {
        return this.directorStorage.findAllDirectors();
    }

    /**
     * Получение режиссера по id
     *
     * @param id id режиссера
     * @return объект Director
     */
    public Director findDirectorById(Integer id) {
        return directorStorage.findDirectorById(id);
    }

    /**
     * Добавление данных о режиссере
     *
     * @param director объект Director
     * @return объект Director с id
     */
    public Director addDirector(Director director) {
        return directorStorage.addDirector(director);
    }

    /**
     * Обновление данных о режиссере
     *
     * @param director объект Director
     * @return обновленный объект Director
     */
    public Director updateDirector(Director director) {
        return directorStorage.updateDirector(director);
    }

    /**
     * Удаление данных о режиссере
     *
     * @param id id режиссера
     */
    public void deleteDirector(Integer id) {
        directorStorage.deleteDirector(id);
    }
}
