package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class Director {
    // информация о директоре - идентификатор, имя
    @Positive
    private Integer id;
    @NotBlank
    private String name;
}
