package ru.yandex.practicum.filmorate.storage;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Event;

@Repository
public interface EventStorage {
	List<Event> listEvents(Long id);

	Event addEvent(Long userId, 
			Long entityId, 
			String eventType, 
			String operationType);

	Event getEvent(Long id);

}
