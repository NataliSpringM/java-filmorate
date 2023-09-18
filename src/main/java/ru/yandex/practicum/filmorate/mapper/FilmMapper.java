package ru.yandex.practicum.filmorate.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;

@Component
@AllArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    private final RatingMpaStorage mpaDbStorage;
    private final FilmGenreStorage genreDbStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(LocalDate.parse(rs.getString("release_date")))
                .duration(rs.getInt("duration"))
                .mpa(mpaDbStorage.getRatingMpaById(rs.getInt("rating")))
                .genres(Collections.singleton(genreDbStorage.getGenreById(rs.getInt("genre_id"))))
                .build();
    }
}
