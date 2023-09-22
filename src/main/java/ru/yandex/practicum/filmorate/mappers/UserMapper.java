package ru.yandex.practicum.filmorate.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * создание объекта User
 */
@RequiredArgsConstructor
@Component
public class UserMapper implements RowMapper<User> {

    private final FriendshipStorage friendshipStorage;

    /**
     * создание объекта User
     */
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .name(rs.getString("user_name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(LocalDate.parse(rs.getString("birthday")))
                .friends(friendshipStorage.listUserFriendsId(rs.getLong("user_id")))
                .build();
    }

}

