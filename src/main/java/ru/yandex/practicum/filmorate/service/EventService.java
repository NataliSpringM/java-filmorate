package ru.yandex.practicum.filmorate.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Event;

@Service
public interface EventService {

	List<Event> listUserEvents(Long id); // получить ленту событий пользователя id

	Event addEvent(Long userId,
			Long entityId,
			String eventType,
			String operationType); // добавить событие пользователя id
}
