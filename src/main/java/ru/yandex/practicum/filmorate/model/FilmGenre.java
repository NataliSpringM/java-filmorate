package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class FilmGenre {

    // информация о жанре фильма - id, описание

    Integer id;
    String name;

}
