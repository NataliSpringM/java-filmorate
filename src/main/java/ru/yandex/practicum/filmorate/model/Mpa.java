package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * информация о рейтинге MPA - id, название
 */
@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Mpa {

	Integer id;
	String name;

}
