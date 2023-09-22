package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * создание объекта Review
 */
@Component
public class ReviewMapper implements RowMapper<Review> {

    /**
     * создание объекта Mpa
     */
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getLong("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }

}
