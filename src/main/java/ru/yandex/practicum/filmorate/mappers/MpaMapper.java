package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * создание объекта Mpa
 */
@Component
public class MpaMapper implements RowMapper<Mpa> {

    /**
     * создание объекта Mpa
     */
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {

        return Mpa.builder()
                .id(rs.getInt("rating_mpa_id"))
                .name(rs.getString("rating_mpa_name"))
                .build();
    }

}


