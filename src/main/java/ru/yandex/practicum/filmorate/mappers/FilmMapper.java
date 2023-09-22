package ru.yandex.practicum.filmorate.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmGenreDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * создание объекта Film
 */
@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {

    private final FilmGenreDbStorage filmGenreDbStorage;
    private final DirectorStorage directorStorage;

    /**
     * создание объекта Film
     */
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .likes(rs.getLong("total_likes"))
                //.genres(createGenreListFromSting(rs.getString("genre_id_name")))
                .mpa(Mpa.builder()
                        .id(rs.getInt("rating_mpa_id"))
                        .name(rs.getString("rating_mpa_name"))
                        .build())
                .genres(filmGenreDbStorage.getFilmGenres(rs.getInt("film_id")))
                .directors(directorStorage.getDirectorsByFilmId(rs.getInt("film_id")))
                .build();
    }

}



