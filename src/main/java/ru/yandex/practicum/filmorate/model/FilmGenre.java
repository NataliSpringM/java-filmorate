package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 *  информация о жанре фильма - id, описание
 */

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class FilmGenre {

    Integer id;
    String name;

}
