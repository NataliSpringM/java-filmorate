package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

/**
 * Интерфейс обеспечивает набор методов для работы с объектом Event в хранилище
 */

@Repository
public interface EventStorage {

    /**
     * Получение из хранилища объектов Event по id пользователя.
     *
     * @param id id события
     * @return список событий
     */
    List<Event> listEvents(Long id);

    /**
     * Получение из хранилища объекта Event по id.
     *
     * @param id id пользователя
     * @return список событий
     */
    Event getEvent(Long id);

    /**
     * Добавление события для пользователя с заданными параметрами.
     *
     * @param userId        id пользователя
     * @param entityId      id события
     * @param eventType     тип события
     * @param operationType тип операции
     * @return объект Event
     */
    Event addEvent(Long userId, Long entityId, String eventType, String operationType);

}
