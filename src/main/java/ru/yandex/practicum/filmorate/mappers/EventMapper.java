package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * создание объекта Event
 */
@Component
public class EventMapper implements RowMapper<Event> {

    /**
     * создание объекта Event
     */
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getLong("event_id"))
                .userId(rs.getLong("user_id"))
                .entityId(rs.getLong("entity_id"))
                .eventType(Event.EventType.fromName(rs.getString("event_type")))
                .operation(Event.OperationType.fromName(rs.getString("operation_type")))
                .timestamp(rs.getLong("time_stamp"))
                .build();
    }


}
