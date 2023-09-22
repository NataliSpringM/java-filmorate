package ru.yandex.practicum.filmorate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Event;

/**
 * сервис для добавления событий в ленту
 */

@Service
public interface EventService {

    /**
     * получить ленту событий пользователя id
     *
     * @param id id события
     * @return объект Event
     */
    List<Event> listUserEvents(Long id);

    /**
     * добавить событие пользователя id
     *
     * @param userId        id пользователя
     * @param entityId      id события
     * @param eventType     тип события
     * @param operationType тип операции
     * @return объект Event с c id
     */
    Event addEvent(Long userId,
                   Long entityId,
                   String eventType,
                   String operationType);
}
