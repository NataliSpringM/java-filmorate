package ru.yandex.practicum.filmorate.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.EventStorage;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private final EventStorage eventStorage;
	private final UserService userService;

	@Override
	public List<Event> listUserEvents(Long id) {
		userService.getUserById(id);
		return this.eventStorage.listEvents(id);
	}

	@Override
	public Event addEvent(Long userId, Long entityId, String eventType, String operationType) {
		userService.getUserById(userId);
		return this.eventStorage.addEvent(userId, entityId, eventType, operationType);
	}


}
