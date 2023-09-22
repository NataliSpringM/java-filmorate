package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;


/**
 * информация о пользователе - id, email, имя (необязательное поле), логин, дата
 * рождения, друзья (список id)
 */
@Validated
@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class User {


    Long id; // id пользователя

    @Email
    @NotNull
    String email; // email пользователя

    @Nullable
    String name; // имя пользователя - необязательное поле

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^\\S+$", message = "не должно содержать пробелы")
    String login; // login пользователя

    @PastOrPresent
    @NotNull
    LocalDate birthday; // дата рождения пользователя

    Set<Long> friends; // список id друзей пользователя

    public Map<String, Object> toMap() {

        Map<String, Object> userProperties = new HashMap<>();

        userProperties.put("email", email);

        if (name == null || name.isBlank()) {
            userProperties.put("user_name", login);
        } else {
            userProperties.put("user_name", name);
        }

        userProperties.put("login", login);
        userProperties.put("birthday", birthday);

        return userProperties;
    }

}