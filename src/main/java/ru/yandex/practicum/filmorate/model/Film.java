package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validation.annotations.CheckReleaseDate;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.*;

@Validated
@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Film {

    // информация о фильме - id, название, описание, дата выпуска, продолжительность фильма, количество лайков
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    Integer id; // id фильма

    @NotNull
    @NotBlank
    @NotEmpty
    String name; // название фильма

    @NotNull
    @Size(max = MAX_DESCRIPTION_LENGTH)
    String description; // описание фильма

    @CheckReleaseDate(message = "не должно быть ранее 28-12-1895")
    LocalDate releaseDate; // дата выхода фильма

    @NotNull
    @Positive
    Integer duration; // продолжительность фильма
    Long likes; // количество лайков
    Mpa mpa; // рейтинг MPA
    Set<FilmGenre> genres; // информация о жанрах фильма

    public Map<String, Object> toMap() {

        Map<String, Object> filmProperties = new HashMap<>();

        filmProperties.put("film_name", name);
        filmProperties.put("description", description);
        filmProperties.put("release_date", releaseDate);
        filmProperties.put("duration", duration);
        filmProperties.put("rating_mpa_id", mpa.getId());

        return filmProperties;
    }

}
