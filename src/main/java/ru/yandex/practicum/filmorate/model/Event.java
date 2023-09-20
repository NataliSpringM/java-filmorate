package ru.yandex.practicum.filmorate.model;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.validation.annotation.Validated;

import lombok.Builder;
import lombok.Data;

@Validated
@Data
@Builder(toBuilder = true)
public class Event implements Comparable<Event> {

	Long eventId; // id события

    @NotNull
    @NotBlank
    @NotEmpty
    Long userId; // id пользователя инициатора

    @NotNull
    @NotBlank
    @NotEmpty
    Long entityId; // id объекта события

    @NotNull
    String eventType; // тип события

    @NotNull
    String operation; // тип операции

    @PastOrPresent
    Long timestamp; // дата и время события

    public Map<String, Object> toMap() {

        Map<String, Object> eventProperties = new HashMap<>();

        eventProperties.put("event_id", eventId);
        eventProperties.put("user_id", userId);
        eventProperties.put("entity_id", entityId);
        eventProperties.put("event_type", eventType);
        eventProperties.put("operation_type", operation);
        eventProperties.put("time_stamp", timestamp);

        return eventProperties;
    }

    @Override
	public int compareTo(Event otherEvent) {
		return this.getTimestamp()
				.compareTo(otherEvent.getTimestamp());
	}
}
