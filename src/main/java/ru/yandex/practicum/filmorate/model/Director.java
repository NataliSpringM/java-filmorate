package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * информация о директоре - идентификатор, имя
 */
@Validated
@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Director {

    @Positive
    Integer id;
    @NotBlank
    String name;

}
