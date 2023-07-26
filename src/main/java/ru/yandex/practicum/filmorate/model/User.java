package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Validated
public class User {

    //информация о пользователе - id, email, имя (необязательное поле), логин, дата рождения

    private Integer id;

    @Email
    @NotNull
    private final String email;

    @Nullable
    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^\\S+$", message = "не должно содержать пробелы")
    private final String login;

    @PastOrPresent
    @NotNull
    private final LocalDate birthday;

}
