package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Event;

/**
 * Интерфейс обеспечивает набор методов для работы с объектом Event в хранилище
 */

@Repository
public interface EventStorage {

	/**
	 * Получение из хранилища объектов Event по id пользователя.
	 *
	 * @param id
	 * @return
	 */
	List<Event> listEvents(Long id);

	/**
	 * Получение из хранилища объекта Event по id.
	 *
	 * @param id
	 * @return
	 */
	Event getEvent(Long id);

	/**
	 * Добавление события для пользователя с заданными параметрами.
	 *
	 * @param userId
	 * @param entityId
	 * @param eventType
	 * @param operationType
	 * @return
	 */
	Event addEvent(Long userId, Long entityId, String eventType, String operationType);

}
