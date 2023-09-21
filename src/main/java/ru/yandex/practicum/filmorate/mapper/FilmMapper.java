package ru.yandex.practicum.filmorate.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.db.RatingMpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    private final RatingMpaDbStorage ratingMpaDbStorage;
    private final FilmGenreDbStorage filmStorage;

    @Override
    public Film mapRow(ResultSet resultSet, int i) throws SQLException {
        Integer id = resultSet.getInt("film_id");
        String name = resultSet.getString("film_name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        Integer duration = resultSet.getInt("duration");
        //Long likes = resultSet.getLong("total_likes");
        Mpa mpa = ratingMpaDbStorage.getRatingMpaById(resultSet.getInt("rating_mpa_id"));
        Set<FilmGenre> genres = filmStorage.getGenres(resultSet.getInt("film_id"));
        Set<Director> directors = new HashSet<>();
        directors.add(new Director());

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                //.likes(likes)
                .mpa(mpa)
                .genres(genres)
                .directors(directors)
                .build();
    }
}
