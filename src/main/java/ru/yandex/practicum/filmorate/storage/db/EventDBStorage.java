package ru.yandex.practicum.filmorate.storage.db;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Event.EventType;
import ru.yandex.practicum.filmorate.storage.EventStorage;
/**
 *  реализация сохранения и получения информации о событиях в базе данных
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class EventDBStorage implements EventStorage {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<Event> listEvents(Long id) {
		// запрашиваем список всех событий пользователя
        String sqlEvents = "SELECT * FROM events where user_id = " + id;

        // обрабатываем запрос и возвращаем список объектов пользователей
        return jdbcTemplate.query(sqlEvents, (rs, rowNum) -> Event.builder()
        		.eventId(rs.getLong("event_id"))
        		.userId(rs.getLong("user_id"))
        		.entityId(rs.getLong("entity_id"))
        		.eventType(Event.EventType.fromName(rs.getString("event_type")))
        		.operation(Event.OperationType.fromName(rs.getString("operation_type")))
        		.timestamp(rs.getLong("time_stamp"))
        		.build());
	}

	@Override
	public Event addEvent(Long userId,
			Long entityId,
			String eventType,
			String operationType) {
		// вставляем данные события в базу данных и получаем сгенерированный id

		SimpleJdbcInsert eventInsertion = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("event_id");

        Event event = Event.builder()
        		.userId(userId)
        		.entityId(entityId)
        		.eventType(Event.EventType.fromName(eventType))
        		.operation(Event.OperationType.fromName(operationType))
        		.timestamp(System.currentTimeMillis())
        		.build();

        Long eventId = eventInsertion.executeAndReturnKey(event.toMap()).longValue();

        // возвращаем данные события с присвоенным id
        Event newEvent = getEvent(eventId);

        log.info("Создано событие: {} ", newEvent);

        return newEvent;
	}

	@Override
	public Event getEvent(Long id) {
		// выполняем запрос к базе данных
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM events WHERE event_id = ?", id);
        Event event;
        // создание объекта события из таблиц, включающих данные о событиях
        if (rs.next()) {
            event = Event.builder()
            		.eventId(rs.getLong("event_id"))
            		.userId(rs.getLong("user_id"))
            		.entityId(rs.getLong("entity_id"))
            		.eventType(Event.EventType.fromName(rs.getString("event_type")))
            		.operation(Event.OperationType.fromName(rs.getString("operation_type")))
            		.timestamp(rs.getLong("time_stamp"))
            		.build();
        } else {

            // сообщение об ошибке и выброс исключения при остутствии пользователя в базе данных
            log.info("Событие с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException(String.format("Событие с id: %d не найден", id));

        }

        log.info("Найдено событие: {} ", id);
        return event;
	}
}
