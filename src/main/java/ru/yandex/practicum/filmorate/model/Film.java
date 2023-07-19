package ru.yandex.practicum.filmorate.model;

import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.annotations.CheckReleaseDate;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Validated
public class Film {

    // информация о фильме - id, название, описание, дата выпуска, продолжительность фильма
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private Integer id;

    @NotNull
    @NotBlank
    @NotEmpty
    private final String name;

    @NotNull
    @Size(max = MAX_DESCRIPTION_LENGTH)
    private final String description;

    @CheckReleaseDate(message = "не должно быть ранее 28-12-1895")
    private final LocalDate releaseDate;

    @NotNull
    @Positive
    private final Integer duration;

}
