package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * создание объекта Director
 */
@Component
public class DirectorMapper implements RowMapper<Director> {

    /**
     * создание объекта Director
     */
    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {

        return Director.builder()
                .id(rs.getInt("director_id"))
                .name(rs.getString("director_name"))
                .build();

    }

}
