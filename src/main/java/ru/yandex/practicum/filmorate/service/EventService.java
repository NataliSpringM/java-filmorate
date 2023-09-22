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
	 * @param id
	 * @return
	 */
	List<Event> listUserEvents(Long id);

	/**
	 * добавить событие пользователя id
	 *
	 * @param userId
	 * @param entityId
	 * @param eventType
	 * @param operationType
	 * @return
	 */
	Event addEvent(Long userId, Long entityId, String eventType, String operationType);
}
