package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 *  информация о директоре - идентификатор, имя
 */
@Data
public class Director {

    @Positive
    private Integer id;
    @NotBlank
    private String name;
}
